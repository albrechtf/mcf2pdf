/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

import net.sf.mcf2pdf.mcfelements.McfClipart;
import net.sf.mcf2pdf.mcfelements.util.ImageUtil;


/**
 * TODO comment
 */
public class PageClipart implements PageDrawable {
	
	private McfClipart clipart;
	
	public PageClipart(McfClipart clipart) {
		this.clipart = clipart;
	}
	
	@Override
	public float getLeftMM() {
		return clipart.getArea().getLeft() / 10.0f;
	}
	
	@Override
	public float getTopMM() {
		return clipart.getArea().getTop() / 10.0f;
	}

	@Override
	public boolean isVectorGraphic() {
		return true;
	}

	@Override
	public void renderAsSvgElement(Writer writer, PageRenderContext context) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BufferedImage renderAsBitmap(PageRenderContext context,
			Point drawOffsetPixels) throws IOException {
		File f = context.getClipart(clipart.getUniqueName());
		if (f == null) {
			context.getLog().warn("Clipart not found: " + clipart.getUniqueName());
			return null;
		}
		context.getLog().debug("Rendering clipart " + f);
		
		int widthPixel = context.toPixel(clipart.getArea().getWidth() / 10.0f);
		int heightPixel = context.toPixel(clipart.getArea().getHeight() / 10.0f);

		drawOffsetPixels.x = drawOffsetPixels.y = 0;
		return ImageUtil.loadClpFile(f, widthPixel, heightPixel);
	}

	@Override
	public int getZPosition() {
		return clipart.getArea().getZPosition();
	}

}
