package net.sf.mcf2pdf.mcfelements.util.webp;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * Utility class to load the dynamic library for the WebP file format from the
 * CEWE installation directory, and for convenient WebP image loading using that
 * library.
 *
 * @author Florian Albrecht
 *
 * @see WebpLib
 *
 */
public final class Webp {

	private Webp() {
	}

	/**
	 * Loads the WebP library. Under Windows, the library must be named
	 * <code>libwebp.dll</code>, under Linux and Mac OS X, a
	 * <code>libwebp.so</code> is expected. For Windows and Linux, the shared
	 * library is included in the mcf2pdf package.
	 *
	 * @return The loaded WebP library, as a dynamic Java JNA proxy.
	 *
	 * @throws IOException
	 *             If the library cannot be found.
	 */
	public static WebpLib loadLibrary() throws IOException {
		String libName = Platform.isWindows() ? "libwebp.dll" : "libwebp.so";
		return (WebpLib) Native.loadLibrary(libName, WebpLib.class);
	}

	/**
	 * Uses the Qt5 library functions to read the given WebP image file into a
	 * {@link BufferedImage}.
	 *
	 * @param webpImageFile
	 *            WebP image file to read.
	 * @param library
	 *            Qt5 library to use. Must have been retrieved using
	 *            {@link #loadLibrary(File)}.
	 * @return The loaded WebP image, as a {@link BufferedImage}.
	 * @throws IOException
	 *             If the image could not be loaded.
	 */
	public static BufferedImage loadWebPImage(File webpImageFile, WebpLib library) throws IOException {
		int[] aw = new int[1];
		int[] ah = new int[1];

		FileInputStream fis = new FileInputStream(webpImageFile);
		byte[] rawData;
		try {
			rawData = IOUtils.toByteArray(fis);
		} finally {
			IOUtils.closeQuietly(fis);
		}

		library.WebPGetInfo(rawData, rawData.length, aw, ah);
		int width = aw[0];
		int height = ah[0];

		if (width == 0 || height == 0 || width > 10000 || height > 10000) {
			throw new IOException("Invalid WebP file");
		}

		// allocate BufferedImage including byte buffer
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		DataBufferByte dbb = (DataBufferByte) img.getRaster().getDataBuffer();
		byte[] target = dbb.getData();

		if (library.WebPDecodeRGBAInto(rawData, rawData.length, target, target.length, width * 4) == null) {
			throw new IOException("Could not read WebP file");
		}

		// bring bytes into correct order (RGBA -> ABGR)
		for (int i = 0; i < target.length; i += 4) {
			byte r = target[i];
			byte g = target[i + 1];
			byte b = target[i + 2];
			byte a = target[i + 3];
			target[i] = a;
			target[i + 1] = b;
			target[i + 2] = g;
			target[i + 3] = r;
		}

		return img;
	}

}
