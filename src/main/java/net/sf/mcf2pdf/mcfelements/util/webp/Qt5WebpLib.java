package net.sf.mcf2pdf.mcfelements.util.webp;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * JNA interface with all required methods to be dynamically linked and accessed
 * from the shared Qt5 library for WebP access. Derived from <a href=
 * "https://code.woboq.org/qt5/qtimageformats/src/3rdparty/libwebp/src/webp/decode.h.html">https://code.woboq.org/qt5/qtimageformats/src/3rdparty/libwebp/src/webp/decode.h.html</a>
 *
 * @author Florian Albrecht
 *
 */
public interface Qt5WebpLib extends Library {

	int WebPGetInfo(byte[] data, int data_size, int[] width, int[] height);

	Pointer WebPDecodeRGBAInto(byte[] data, int data_size, byte[] output_buffer, int output_buffer_size,
			int output_stride);

}
