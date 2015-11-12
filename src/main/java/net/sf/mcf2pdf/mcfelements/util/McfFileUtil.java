/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.util;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.mcf2pdf.mcfelements.McfFotobook;


/**
 * Utility class for File related "hacks" in the context of the mcf2pdf project.
 */
public final class McfFileUtil {
	
	private McfFileUtil() {
	}
	
	/**
	 * Tries some locations to find the given image file:
	 * <nl><li>First, it searches for the images directory within the
	 * directory where the fotobook is stored, and if found, searches there for
	 * the given file.</li>
	 * <li>If not found, it is checked if the image dir exists in the CURRENT
	 * working directory, and if exists, it is searched for the file.</li>
	 * <li>As a last try, the given file is searched directly from where the
	 * fotobook is stored (this is required for some older mcf files).</li>
	 * </nl>
	 * 
	 * @param fileName File name to search. Should be the <code>filename</code>
	 * property of an <code>image</code> or <code>imagebackground</code> tag.
	 * @param fotobook The fotobook to use for search.
	 *  
	 * @return The file, if found.
	 * 
	 * @throws FileNotFoundException If the file could not be found.
	 */
	public static File getImageFile(String fileName, McfFotobook fotobook) throws FileNotFoundException {
		File f = new File(new File(fotobook.getFile().getParentFile(), fotobook.getImageDir()), fileName);
		if (f.isFile())
			return f;
		
		f = new File(new File(fotobook.getImageDir()), fileName);
		if (f.isFile())
			return f;
		
		f = new File(fotobook.getFile().getParentFile(), fileName);
		if (f.isFile())
			return f;

		throw new FileNotFoundException(fileName);
	}

}
