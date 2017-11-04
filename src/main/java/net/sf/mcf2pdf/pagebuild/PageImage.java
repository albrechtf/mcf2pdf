/*******************************************************************************
 * ${licenseText}
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

import net.sf.mcf2pdf.mcfelements.McfBorder;
import net.sf.mcf2pdf.mcfelements.McfImage;
import net.sf.mcf2pdf.mcfelements.util.FadingComposite;
import net.sf.mcf2pdf.mcfelements.util.ImageUtil;
import net.sf.mcf2pdf.mcfelements.util.McfFileUtil;
import net.sf.mcf2pdf.mcfglobals.McfFotoFrame;
import net.sf.mcf2pdf.mcfconfig.Fading;
import net.sf.mcf2pdf.mcfconfig.Fotoarea;


/**
 * TODO comment
 */
public class PageImage implements PageDrawable {

	private McfImage image;

	public PageImage(McfImage image) {
		this.image = image;
	}

	@Override
	public float getLeftMM() {
		return image.getArea().getLeft() / 10.0f;
	}

	@Override
	public float getTopMM() {
		return image.getArea().getTop() / 10.0f;
	}

	@Override
	public int getZPosition() {
		return image.getArea().getZPosition();
	}

	@Override
	public boolean isVectorGraphic() {
		return false;
	}

	@Override
	public void renderAsSvgElement(Writer writer,	PageRenderContext context) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public BufferedImage renderAsBitmap(PageRenderContext context, Point drawOffsetPixels) throws IOException {
		int widthPixel = context.toPixel(image.getArea().getWidth() / 10.0f);
		int heightPixel = context.toPixel(image.getArea().getHeight() / 10.0f);
		if (image.getFileName() == null || "".equals(image.getFileName())) {
			// return empty image
			return new BufferedImage(widthPixel, heightPixel, BufferedImage.TYPE_INT_ARGB);
		}

		File baseImage = McfFileUtil.getImageFile(image.getFileName(),
				image.getArea().getPage().getFotobook());

		context.getLog().debug("Rendering image " + baseImage);

		// check for "fading" file
		File maskFile = null;
		File clipartFile = null;
		Fotoarea fotoArea = null;
		if (image.getFadingFile() != null) {
			McfFotoFrame frame = context.getFotoFrame(image.getFadingFile());
			if (frame != null && frame.getFading() != null) {
				maskFile = frame.getFading();
			} else {
				maskFile = context.getFading(image.getFadingFile());
			}
			if (frame != null && frame.getClipart() != null) {
				clipartFile = frame.getClipart();
			}
			if (frame != null) {
				Fading config = frame.getConfig();
				if (config != null) {
					fotoArea = config.getFotoarea();
				}
			}
			if (maskFile == null) 
				context.getLog().warn("Could not find fading file: " + image.getFadingFile());
		}

		// get image resolution; load base image into memory
		float[] res = ImageUtil.getImageResolution(baseImage);
		BufferedImage baseImg = ImageUtil.readImage(baseImage);

		double tmmX = image.getArea().getWidth() /10.0f;
		double tmmY = image.getArea().getHeight() / 10.0f;

		double resX = res[0] / ImageUtil.MM_PER_INCH;
		double resY = res[1] / ImageUtil.MM_PER_INCH;

		double scale = image.getScale() / ImageUtil.SQRT_2;

		double sw = (tmmX * resX) / scale;
		double sh = (tmmY * resY) / scale;

		// draw border and / or shadow?
		int borderWidth = (maskFile != null || !image.getArea().isBorderEnabled()) ? 0
				: context.toPixel(image.getArea().getBorderSize() / 10.0f);
		Color borderColor = image.getArea().getBorderColor();
		// check for Format 6 - border may be child element
		if (image.getArea().getBorder() != null) {
			McfBorder border = image.getArea().getBorder();
			borderWidth = border.isEnabled() ? context.toPixel(border.getWidth() / 10.0f) : 0;
			borderColor = border.getColor();
		}

		int shadowDistance = (maskFile != null | !image.getArea().isShadowEnabled()) ? 0 : context.toPixel(image.getArea().getShadowDistance() / 10.0f);
		int xAddShadow = (int)Math.round(shadowDistance * Math.sin(Math.toRadians(image.getArea().getShadowAngle())));
		int yAddShadow = (int)Math.round(shadowDistance * -Math.cos(Math.toRadians(image.getArea().getShadowAngle())));
		int xAdd = Math.max(Math.abs(xAddShadow), borderWidth * 2);
		int yAdd = Math.max(Math.abs(yAddShadow), borderWidth * 2);

		// hide shadow when completely occupied by border
		if (Math.abs(xAddShadow) <= borderWidth && Math.abs(yAddShadow) <= borderWidth)
			shadowDistance = 0;

		// create image without rotation
		BufferedImage img = new BufferedImage(widthPixel + xAdd, heightPixel + yAdd,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();

		int imgLeft = 0, imgTop = 0;

		// draw shadow
		if (shadowDistance > 0) {
			int sleft = xAddShadow < 0 ? 0 : xAddShadow;
			int stop = yAddShadow < 0 ? 0 : yAddShadow;
			imgLeft = sleft == 0 ? -xAddShadow : 0;
			imgTop = stop == 0 ? -yAddShadow : 0;

			g2d.setColor(new Color(0, 0, 0, image.getArea().getShadowIntensity()));
			g2d.fillRect(sleft, stop, widthPixel, heightPixel);
		}

		// draw border
		if (borderWidth != 0) {
			int bleft = xAddShadow < 0 ? Math.max(0, -xAddShadow - borderWidth) : 0;
			int btop = yAddShadow < 0 ? Math.max(0, -yAddShadow - borderWidth) : 0;
			imgLeft = bleft + borderWidth;
			imgTop = btop + borderWidth;
			g2d.setColor(borderColor);
			g2d.fillRect(bleft, btop, widthPixel + 2 * borderWidth, heightPixel + 2 * borderWidth);
		}

		int leftOffset = -image.getLeft();
		int topOffset = -image.getTop();
		
		drawOffsetPixels.x = -imgLeft;
		drawOffsetPixels.y = -imgTop;
		
		int effImgWidth = widthPixel;
		int effImgHeight = heightPixel;
		if (fotoArea != null) {
			imgLeft += (int)Math.round(fotoArea.getX() * widthPixel);
			imgTop += (int)Math.round(fotoArea.getY() * heightPixel);
			effImgWidth = (int)Math.round(widthPixel * fotoArea.getWidth());
			effImgHeight = (int)Math.round(heightPixel * fotoArea.getHeight());
			sw = sw * fotoArea.getWidth();
			sh = sh * fotoArea.getHeight();
		}

		// draw main image
		g2d.drawImage(baseImg, 
				imgLeft, imgTop, imgLeft + effImgWidth, imgTop + effImgHeight,
				leftOffset, topOffset, leftOffset + (int)Math.round(sw), topOffset + (int)Math.round(sh),
				null);
		
		// mask image
		if (maskFile != null) {
			int x = 0;
			int y = 0;
			int effWidth = widthPixel;
			int effHeight = heightPixel;
			if (fotoArea != null) {
				x = (int)(fotoArea.getX() * widthPixel);
				y = (int)(fotoArea.getY() * heightPixel);
				effWidth = (int)(widthPixel * fotoArea.getWidth());
				effHeight = (int)(heightPixel * fotoArea.getHeight());
			}
			
			context.getLog().debug("Applying fading file " + maskFile);
			BufferedImage imgMask = ImageUtil.loadClpFile(maskFile, effWidth, effHeight);

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setComposite(FadingComposite.INSTANCE);

			g2d.drawImage(imgMask, x, y, null);
			g2d.setPaintMode();
		}
		
		// clipart image
		if (clipartFile != null) {
			int x = 0;
			int y = 0;
			context.getLog().debug("Applying clipart file " + maskFile);
			BufferedImage imgClipart = ImageUtil.loadClpFile(clipartFile, widthPixel, heightPixel);
			
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(imgClipart, x, y, null);
		}

		g2d.dispose();

		// apply rotation
		if (image.getArea().getRotation() != 0) {
			img = ImageUtil.rotateImage(img, (float)Math.toRadians(image.getArea().getRotation()), drawOffsetPixels);
		}

		return img;
	}

}