/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Font;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.mcf2pdf.mcfelements.util.ImageUtil;
import net.sf.mcf2pdf.mcfglobals.McfAlbumType;
import net.sf.mcf2pdf.mcfglobals.McfResourceScanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The context for page based rendering. The context offers information about
 * the output DPI settings and the album type in use, and provides methods to
 * retrieve referenced resources. Also, a log object can be retrieved to log
 * rendering related messages.
 */
public final class PageRenderContext {
	
	private final static Log log = LogFactory.getLog(PageRenderContext.class);
	
	private int targetDpi;
	
	private McfResourceScanner resources;
	
	private McfAlbumType albumType;
	
	public PageRenderContext(int targetDpi, McfResourceScanner resources,
			McfAlbumType albumType) {
		this.targetDpi = targetDpi;
		this.resources = resources;
		this.albumType = albumType;
	}
	
	/**
	 * Returns a log object which can be used to log rendering related messages.
	 * 
	 * @return A log object which can be used to log rendering related messages.
	 */
	public Log getLog() {
		return log;
	}
	
	/**
	 * Returns the target DPI setting.
	 * 
	 * @return The target DPI setting.
	 */
	public int getTargetDpi() {
		return targetDpi;
	}
	
	/**
	 * Returns the album type in use for the current MCF file. The album type
	 * contains information about page sizes etc.
	 * 
	 * @return The album type in use for the current MCF file.
	 */
	public McfAlbumType getAlbumType() {
		return albumType;
	}
	
	/**
	 * Returns the image file containing the "binding" image, if any. This is taken
	 * from the installation directory of the MCF software.
	 * 
	 * @return The image file containing the "binding" image, if any.
	 */
	public File getBinding() {
		return resources.getBinding();
	}
	
	private static final Pattern PATTERN_FADING = Pattern.compile("fading_(.+)\\.svg", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_CLIPART = Pattern.compile("clipart_(.+)\\.svg", Pattern.CASE_INSENSITIVE);
	
	/**
	 * Returns the "fading" (mask) file for the given referenced file name 
	 * (should end with .svg). The installation and the temporary directories 
	 * of the MCF software are searched for an according CLP file.
	 * 
	 * @param fileName SVG file name, something like <code>fading_foo.svg</code>.
	 * 
	 * @return The CLP file containing the vector mask, or <code>null</code> if not found.
	 */
	public File getFading(String fileName) {
		Matcher m = PATTERN_FADING.matcher(fileName);
		if (!m.matches())
			return null;
		
		return resources.getClip(m.group(1));
	}
	
	/**
	 * Returns the clipart file for the given referenced file name (should end with
	 * .svg). The installation and the temporary directories of the MCF software
	 * are searched for an according CLP file.
	 * 
	 * @param fileName SVG file name, something like <code>clipart_foo.svg</code>.
	 * 
	 * @return The CLP file containing the vector graphic, or <code>null</code> if not found.
	 */
	public File getClipart(String fileName) {
		Matcher m = PATTERN_CLIPART.matcher(fileName);
		if (!m.matches())
			return null;
		
		return resources.getClip(m.group(1));
	}
	
	/**
	 * Returns the background image for the given ID (usually a number). The 
	 * installation and the temporary directories of the MCF software are searched 
	 * for an according JPEG file.
	 * 
	 * @param id ID of the background image (a number).
	 * 
	 * @return The JPEG file containing the image, or <code>null</code> if not found.
	 */
	public File getBackgroundImage(String id) {
		return resources.getImage(id);
	}
	
	/**
	 * Return the background image for given color name eg. 'Schwarz'.
	 * 
	 * @param name Name of the color
	 * @return The JPEG file containing the image, or <code>null</code> if not found.
	 */
	public File getBackgroundColor(String name) {
		return resources.getColorImage(name);
	}
	
	/**
	 * Converts the given millimeter value to pixels, using the DPI setting of this
	 * context.
	 * 
	 * @param mm millimeter value.
	 * 
	 * @return Pixel value, according to the current DPI settings.
	 */
	public int toPixel(float mm) {
		return Math.round(mm * (targetDpi / ImageUtil.MM_PER_INCH));
	}

	/**
	 * Returns the font with the given name, if such a font is present in the
	 * MCF software. The installation and the temporary directories of the MCF 
	 * software are searched for an according TTF file.
	 * 
	 * @param fontFamily Family name of the font.
	 * 
	 * @return A loaded Font object, or <code>null</code> if this font is not
	 * present in the MCF software.
	 */
	public Font getFont(String fontFamily) {
		return resources.getFont(fontFamily);
	}
}
