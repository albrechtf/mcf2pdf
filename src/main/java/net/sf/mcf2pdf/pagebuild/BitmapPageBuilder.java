/*******************************************************************************
 * ${licenseText}
 * All rights reserved. This file is made available under the terms of the
 * Common Development and Distribution License (CDDL) v1.0 which accompanies
 * this distribution, and is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import net.sf.mcf2pdf.mcfelements.util.XslFoDocumentBuilder;

import org.jdom.Element;
import org.jdom.Namespace;



public class BitmapPageBuilder extends AbstractPageBuilder {

	private File tempImageDir;

	private PageRenderContext context;

	private float widthMM;

	private float heightMM;

	private static final Comparator<PageDrawable> zComp = new Comparator<PageDrawable>() {
		@Override
		public int compare(PageDrawable p1, PageDrawable p2) {
			return p1.getZPosition() - p2.getZPosition();
		}
	};

	public BitmapPageBuilder(float widthMM, float heightMM,
			PageRenderContext context, File tempImageDir) throws IOException {
		this.widthMM = widthMM;
		this.heightMM = heightMM;
		this.context = context;
		this.tempImageDir = tempImageDir;
	}

	@Override
	public void addToDocumentBuilder(XslFoDocumentBuilder docBuilder)
			throws IOException {
		// render drawables onto image, regardless of type
		List<PageDrawable> pageContents = new Vector<PageDrawable>(getDrawables());
		Collections.sort(pageContents, zComp);

		context.getLog().debug("Creating full page image from page elements");

		BufferedImage img = new BufferedImage(context.toPixel(widthMM),
				context.toPixel(heightMM), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, img.getWidth(), img.getHeight());

		for (PageDrawable pd : pageContents) {
			int left = context.toPixel(pd.getLeftMM());
			int top = context.toPixel(pd.getTopMM());

			Point offset = new Point();
			try {
				BufferedImage pdImg = pd.renderAsBitmap(context, offset);
				if (pdImg != null)
					g2d.drawImage(pdImg, left + offset.x, top + offset.y, null);
			}
			catch (FileNotFoundException e) {
				// ignore
				// throw e;
			}
		}

		docBuilder.addPageElement(createXslFoElement(img, docBuilder.getNamespace()), widthMM, heightMM);
		g2d.dispose();
	}

	private Element createXslFoElement(BufferedImage img, Namespace xslFoNs) throws IOException {
		// save bitmap to file
		File f;
		int i = 1;
		do {
			f = new File(tempImageDir, (i++) + ".jpg");
		}
		while (f.isFile());

		BufferedImage imgPlain = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = imgPlain.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();

		ImageIO.write(imgPlain, "jpeg", f);

		Element eg = new Element("external-graphic", xslFoNs);
		eg.setAttribute("src", f.getAbsolutePath());
		eg.setAttribute("content-width", widthMM + "mm");
		eg.setAttribute("content-height", heightMM + "mm");
		f.deleteOnExit();

		return eg;
	}


}
