/*******************************************************************************
 * ${licenseText}
 *******************************************************************************/
package net.sf.mcf2pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.sf.mcf2pdf.mcfelements.FotobookBuilder;
import net.sf.mcf2pdf.mcfelements.McfArea;
import net.sf.mcf2pdf.mcfelements.McfBackground;
import net.sf.mcf2pdf.mcfelements.McfClipart;
import net.sf.mcf2pdf.mcfelements.McfFotobook;
import net.sf.mcf2pdf.mcfelements.McfImage;
import net.sf.mcf2pdf.mcfelements.McfImageBackground;
import net.sf.mcf2pdf.mcfelements.McfPage;
import net.sf.mcf2pdf.mcfelements.McfText;
import net.sf.mcf2pdf.mcfelements.util.XslFoDocumentBuilder;
import net.sf.mcf2pdf.mcfglobals.McfAlbumType;
import net.sf.mcf2pdf.mcfglobals.McfProductCatalogue;
import net.sf.mcf2pdf.mcfglobals.McfResourceScanner;
import net.sf.mcf2pdf.pagebuild.BitmapPageBuilder;
import net.sf.mcf2pdf.pagebuild.PageBackground;
import net.sf.mcf2pdf.pagebuild.PageBinding;
import net.sf.mcf2pdf.pagebuild.PageBuilder;
import net.sf.mcf2pdf.pagebuild.PageClipart;
import net.sf.mcf2pdf.pagebuild.PageImage;
import net.sf.mcf2pdf.pagebuild.PageImageBackground;
import net.sf.mcf2pdf.pagebuild.PageRenderContext;
import net.sf.mcf2pdf.pagebuild.PageText;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShlObj;
import com.sun.jna.platform.win32.WinDef;

/**
 * The core class to convert MCF files to XSL-FO data (which then can be used
 * to render page based documents, e.g. PDF files).
 */
public class Mcf2FoConverter {

	private final static Log log = LogFactory.getLog(Mcf2FoConverter.class);

	private Set<McfProductCatalogue> productCatalogues = new HashSet<McfProductCatalogue>();

	private McfResourceScanner resources;

	private McfAlbumType albumType;

	private PageBuilder currentPage;

	private PageRenderContext context;

	private File tempImageDir;

	/**
	 * Creates a new converter object with given settings.
	 *
	 * @param mcfInstallDir The installation directory of the MCF Software.
	 * @param mcfTempDir The temporary directory of the MCF Software to use. This
	 * is normally <code>&lt;USER_HOME>/.mcf</code>.
	 * @param tempImageDir The temporary directory to use for storing image files
	 * (created by this converter object). This directory is created if it does not exist.
	 *
	 * @throws IOException If any I/O problem occurs when reading files in the
	 * installation directory, or the temporary directory could not be created.
	 *
	 * @throws SAXException If any of the XML files in the installation directory
	 * which are parsed have a format error.
	 */
	public Mcf2FoConverter(File mcfInstallDir, File mcfTempDir, File tempImageDir) throws IOException, SAXException {
		this.tempImageDir = tempImageDir;

		// get all products - scan ALL XML files in Resources for fotobookdefinitions
		File resourcesDir = new File(mcfInstallDir, "Resources");
		if (!resourcesDir.isDirectory()) {
			throw new IOException("MCF Resources Directory " + resourcesDir.getAbsolutePath() + " does not exist");
		}

		scanForProducts(resourcesDir, McfProductCatalogue.CatalogueVersion.PRE_V6);
		File productsDir = new File(resourcesDir, "products");
		if (productsDir.isDirectory()) {
			scanForProducts(productsDir, McfProductCatalogue.CatalogueVersion.V6);
		}

		// search all resources
		// on Windows, also scan "<Common Application Data>/hps", if existing
		File hpsDir = null;
		try {
			char[] pszPath = new char[WinDef.MAX_PATH];
			if (Shell32.INSTANCE.SHGetFolderPath(null, ShlObj.CSIDL_COMMON_APPDATA, null, ShlObj.SHGFP_TYPE_CURRENT, pszPath).intValue() == 0) {
				File f = new File(new File(Native.toString(pszPath)), "hps");
				if (f.isDirectory()) {
					hpsDir = f;
				}
			}
		}
		catch (Throwable t) {
			// OK, no Windows, obviously
			// try home directory
			File homeHpsDir = new File(new File(System.getProperty("user.home")), ".mcf/hps");
			if (homeHpsDir.isDirectory()) {
				hpsDir = homeHpsDir;
			}
		}

		List<File> scanDirs = new ArrayList<File>();
		scanDirs.add(new File(mcfInstallDir, "Resources"));
		scanDirs.add(mcfTempDir);
		if (hpsDir != null) {
			scanDirs.add(hpsDir);
		}
		log.debug("Searching for MCF resources in " + scanDirs);
		resources = new McfResourceScanner(scanDirs);
		resources.scan();
	}

	/**
	 * Performs conversion of the given MCF input file to XSL-FO data.
	 *
	 * @param mcfFile MCF input file.
	 * @param xslFoOut Stream to write the XSL-FO data to.
	 * @param dpi Resolution, in dots per inch, to use for image rendering. The
	 * higher the value, the better the quality, but the more RAM is consumed.
	 * @param maxPageNo Renders only up to the given page number (e.g. for testing settings).
	 * A value of -1 indicates to render all pages.
	 *
	 * @throws IOException If any I/O related problem occurs, e.g. reading the
	 * input file or any referenced image files.
	 *
	 * @throws SAXException If any XML related problem occurs, e.g. the input file
	 * has an invalid format.
	 */
	public void convert(File mcfFile, OutputStream xslFoOut, int dpi, boolean binding, int maxPageNo) throws IOException, SAXException {
		// build MCF DOM
		log.debug("Reading MCF file");
		McfFotobook book = new FotobookBuilder().readFotobook(mcfFile);

		// retrieve global information
		File baseDir = mcfFile.getParentFile().getAbsoluteFile();
		File imageDir = new File(baseDir, book.getImageDir());

		albumType = getAlbumType(book.getProductName());
		// fallback workaround
		if (albumType == null && book.getProductName().contains("_")) {
			String fixedType = book.getProductName().substring(0, book.getProductName().indexOf("_"));
			albumType = getAlbumType(fixedType);
			if (albumType != null)
				log.warn("Album Type " + book.getProductName() + " not found, falling back to " + fixedType);
		}

		if (albumType == null)
			throw new IOException("No album type definition found for used print product '" + book.getProductName() + "'");

		// prepare page render context
		context = new PageRenderContext(dpi, resources, albumType);

		// create XSL-FO document
		XslFoDocumentBuilder docBuilder = new XslFoDocumentBuilder();

		float pageWidth = albumType.getUsableWidth() / 10.0f * 2;
		float pageHeight = albumType.getUsableHeight() / 10.0f;

		// set page master
		docBuilder.addPageMaster("default", pageWidth, pageHeight);

		// create master for cover
		float coverPageWidth = pageWidth + (albumType.getCoverExtraHorizontal() * 2 + albumType.getSpineWidth(book.getNormalPages())) / 10.0f;
		float coverPageHeight = pageHeight + albumType.getCoverExtraVertical() / 10.0f;

		docBuilder.addPageMaster("cover", coverPageWidth, coverPageHeight);

		// prepare temporary folder
		log.debug("Preparing temporary working directory");
		tempImageDir.mkdirs();

		// look for cover
		McfPage leftCover = null;
		McfPage rightCover = null;
		for (McfPage p : book.getPages()) {
			if (McfPage.FULLCOVER.matcher(p.getType()).matches()) {
				if (leftCover == null)
					leftCover = p;
				else
					rightCover = p;
			}
		}

		if (leftCover != null) {
			log.info("Rendering cover...");
			currentPage = new BitmapPageBuilder(coverPageWidth, coverPageHeight, context, tempImageDir);
			processDoublePage(leftCover, rightCover, imageDir, false);
			docBuilder.startFlow("cover");
			currentPage.addToDocumentBuilder(docBuilder);
			docBuilder.endFlow();
		}

		// collect pages
		List<McfPage> normalPages = new Vector<McfPage>();

		for (McfPage p : book.getPages()) {
			if (McfPage.CONTENT.matcher(p.getType()).matches() || McfPage.EMPTY.matcher(p.getType()).matches()) {
				if (maxPageNo < 0 || p.getPageNr() <= maxPageNo)
					normalPages.add(p);
				else
					break;
			}
		}
		if (normalPages.isEmpty()) {
			throw new IOException("No standard pages found for rendering. Perhaps unknown MCF format version?");
		}

		// now, process pages as a pair
		docBuilder.startFlow("default");
		log.debug("Starting rendering of " + normalPages.size() + " pages");
		currentPage = new BitmapPageBuilder(pageWidth, pageHeight, context, tempImageDir);

		for (int i = 0; i < normalPages.size(); i += 2) {
			log.info("Rendering pages " + i + "+" + (i+1) + "...");
			processDoublePage(normalPages.get(i),
					i + 1 < normalPages.size() ? normalPages.get(i + 1) : null,
					imageDir, binding);
			currentPage.addToDocumentBuilder(docBuilder);
			if (i < normalPages.size() - 2) {
				docBuilder.newPage();
				currentPage = new BitmapPageBuilder(pageWidth, pageHeight, context, tempImageDir);
			}
		}

		docBuilder.endFlow();

		// create and write document
		log.debug("Creating XSL-FO data");
		new XMLOutputter().output(docBuilder.createDocument(), xslFoOut);
	}

	private void processDoublePage(McfPage leftPage, McfPage rightPage, File imageDir,
			boolean addBinding) throws IOException {
		// handle packgrounds
		PageBackground bg = new PageBackground(
				leftPage == null ? Collections.<McfBackground>emptyList() : leftPage.getBackgrounds(),
				rightPage == null ? Collections.<McfBackground>emptyList() : rightPage.getBackgrounds());
		currentPage.addDrawable(bg);

		// handle "area" elements
		List<McfArea> areas = new Vector<McfArea>();
		if (leftPage != null)
			areas.addAll(leftPage.getAreas());
		if (rightPage != null)
			areas.addAll(rightPage.getAreas());

		for (McfArea a : areas) {
			if (McfArea.IMAGEBACKGROUNDAREA.matcher(a.getAreaType()).matches()) {
				currentPage.addDrawable(new PageImageBackground((McfImageBackground)a.getContent()));
			}
			else if (McfArea.IMAGEAREA.matcher(a.getAreaType()).matches()) {
				currentPage.addDrawable(new PageImage((McfImage)a.getContent()));
			}
			else if (McfArea.CLIPARTAREA.matcher(a.getAreaType()).matches()) {
				currentPage.addDrawable(new PageClipart((McfClipart)a.getContent()));
			}
			else if (McfArea.TEXTAREA.matcher(a.getAreaType()).matches()) {
				currentPage.addDrawable(new PageText((McfText)a.getContent()));
			}
		}

		if (addBinding) {
			currentPage.addDrawable(new PageBinding(resources.getBinding(),
					(albumType.getUsableWidth() + albumType.getBleedMargin()) / 10.0f * 2,
					(albumType.getUsableHeight() + albumType.getBleedMargin() * 2) / 10.0f));
		}
	}

	private McfAlbumType getAlbumType(String albumTypeName) throws IOException {
		for (McfProductCatalogue cat : productCatalogues) {
			McfAlbumType atype = cat.getAlbumType(albumTypeName);
			if (atype != null)
				return atype;
		}

		return null;
	}

	private void scanForProducts(File productsDir, McfProductCatalogue.CatalogueVersion version) {
		for (File f : productsDir.listFiles((FilenameFilter)FileFilterUtils.suffixFileFilter(".xml"))) {
			// try to read, fail is ok!
			FileInputStream fis = null;
			try {
				log.debug("Checking XML file for product catalogue: " + f);
				fis = new FileInputStream(f);
				McfProductCatalogue cat = McfProductCatalogue.read(fis, version);
				if (cat != null && !cat.isEmpty()) {
					log.debug("Adding product catalogue found in " + f);
					productCatalogues.add(cat);
				}
			}
			catch (Exception e) {
			}
			finally {
				IOUtils.closeQuietly(fis);
			}
		}
	}

}
