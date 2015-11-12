/*******************************************************************************
 * ${licenseText}
 * All rights reserved. This file is made available under the terms of the 
 * Common Development and Distribution License (CDDL) v1.0 which accompanies 
 * this distribution, and is available at 
 * http://www.opensource.org/licenses/cddl1.txt     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.util;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.*;

/**
 * Performs the "fading" of a rendered image with a mask image. 
 * In MCF, the mask images have to be applied as follows:
 * <ul>
 * <li>transparent areas have to be also transparent on target image</li>
 * <li>black areas are "opaque" areas of the target image</li>
 * <li>greyscale areas have to get an alpha value on the target image, 
 * according to the greyscale degree.</li>
 * </ul>
 */
public final class FadingComposite implements Composite {

	public static final FadingComposite INSTANCE = new FadingComposite();

	private FadingComposite() {
	}


	private static boolean validateColorModel(ColorModel cm) {
		if (cm instanceof DirectColorModel
				&& cm.getTransferType() == DataBuffer.TYPE_INT) {
			DirectColorModel directCM = (DirectColorModel)cm;

			return directCM.getRedMask() == 0x00FF0000
					&& directCM.getGreenMask() == 0x0000FF00
					&& directCM.getBlueMask() == 0x000000FF
					&& directCM.getAlphaMask() == 0xFF000000;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public CompositeContext createContext(ColorModel srcColorModel,
			ColorModel dstColorModel, RenderingHints hints) {
		if (!validateColorModel(srcColorModel)
				|| !validateColorModel(dstColorModel)) {
			throw new RasterFormatException("Color models are not compatible");
		}

		return new FadingContext(this);
	}

	private static final class FadingContext implements CompositeContext {
		
		private FadingContext(FadingComposite composite) {
		}

		public void dispose() {
		}

		public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
			int width = Math.min(src.getWidth(), dstIn.getWidth());
			int height = Math.min(src.getHeight(), dstIn.getHeight());

			int[] result = new int[4];
			int[] srcPixel = new int[4];
			int[] dstPixel = new int[4];
			int[] srcPixels = new int[width];
			int[] dstPixels = new int[width];

			for (int y = 0; y < height; y++) {
				src.getDataElements(0, y, width, 1, srcPixels);
				dstIn.getDataElements(0, y, width, 1, dstPixels);
				for (int x = 0; x < width; x++) {
					// pixels are stored as INT_ARGB
					// our arrays are [R, G, B, A]
					int pixel = srcPixels[x];
					srcPixel[0] = (pixel >> 16) & 0xFF;
					srcPixel[1] = (pixel >> 8) & 0xFF;
					srcPixel[2] = (pixel) & 0xFF;
					srcPixel[3] = (pixel >> 24) & 0xFF;

					pixel = dstPixels[x];
					dstPixel[0] = (pixel >> 16) & 0xFF;
					dstPixel[1] = (pixel >> 8) & 0xFF;
					dstPixel[2] = (pixel) & 0xFF;
					dstPixel[3] = (pixel >> 24) & 0xFF;

					process(srcPixel, dstPixel, result);
					// copy back to dstPixels
					dstPixels[x] = ((result[3] & 0xFF) << 24) | ((result[0] & 0xFF) << 16) |
							((result[1] & 0xFF) << 8) | (result[2] & 0xFF);
				}
				dstOut.setDataElements(0, y, width, 1, dstPixels);
			}
		}
	
		// The core "fading" logic
		private void process(int[] src, int[] dst, int[] result) {
			// use colors from dst, calculate alpha from src color
			result[0] = dst[0];
			result[1] = dst[1];
			result[2] = dst[2];
			
			if (src[3] != 255) {
				result[3] = src[3];
			}
			else {
				int alpha = 255 - (int)Math.round((src[0] + src[1] + src[2]) / 3.0);
				result[3] = Math.max(alpha, 0);
			}
		}
	}
}
