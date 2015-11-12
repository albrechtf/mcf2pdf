/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.sf.mcf2pdf.mcfelements.McfImageBackground;
import net.sf.mcf2pdf.mcfglobals.McfAlbumType;


/**
 * TODO comment
 */
public class PageImageBackground extends PageImage {
	
	private McfImageBackground image;

	public PageImageBackground(McfImageBackground image) {
		super(image);
		this.image = image;
	}
	
	@Override
	public BufferedImage renderAsBitmap(PageRenderContext context,
			Point drawOffsetPixels) throws IOException {
		BufferedImage img = super.renderAsBitmap(context, drawOffsetPixels);
		if (McfImageBackground.RIGHT_OR_BOTTOM.equals(image.getBackgroundPosition())) {
			McfAlbumType albumType = context.getAlbumType();
			drawOffsetPixels.x += context.toPixel(albumType.getUsableWidth() / 10.0f + 
					albumType.getBleedMargin() / 10.0f);
		}
		return img;
	}

}
