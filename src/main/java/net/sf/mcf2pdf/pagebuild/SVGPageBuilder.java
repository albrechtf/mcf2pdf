/*******************************************************************************
 * ${licenseText}
 * All rights reserved. This file is made available under the terms of the 
 * Common Development and Distribution License (CDDL) v1.0 which accompanies 
 * this distribution, and is available at 
 * http://www.opensource.org/licenses/cddl1.txt     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import net.sf.mcf2pdf.mcfelements.util.XslFoDocumentBuilder;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.*;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;


/**
 * Vector based renderer for pages. Yet to be implemented.
 */
public class SVGPageBuilder extends AbstractPageBuilder {
	
	private float widthMM;

	private float heightMM;

	private PageRenderContext context;

	private File tempImageDir;

	public SVGPageBuilder(float widthMM, float heightMM, PageRenderContext context, 
			File tempImageDir) throws IOException {
		this.widthMM = widthMM;
		this.heightMM = heightMM;
		this.context = context;
		this.tempImageDir = tempImageDir;
	}
	
	@Override
	public void addToDocumentBuilder(XslFoDocumentBuilder docBuilder)
			throws IOException {
		// Get a DOMImplementation.
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);
		
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
		DefaultImageHandler ihandler = new ImageHandlerJPEGEncoder(tempImageDir.getAbsolutePath(), null);
		ctx.setImageHandler(ihandler);

		// Create an instance of the SVG Generator.
		SVGGraphics2D graphics = new SVGGraphics2D(ctx, false);
		graphics.setSVGCanvasSize(new Dimension(context.toPixel(widthMM), context.toPixel(heightMM)));
		
		
		// TODO render all drawables to canvas
		
		
		graphics.dispose();
	}
	

}
