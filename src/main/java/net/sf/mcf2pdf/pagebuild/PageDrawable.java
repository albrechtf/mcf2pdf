/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Writer;

/**
 * The interface for objects which can be drawn by <code>PageBuilder</code>s.
 * Every <code>PageDrawable</code> must be able to render itself as a bitmap.
 * Optionally, it can also be able to create an SVG element displaying its contents.
 */
public interface PageDrawable {
	
	/**
	 * Indicates if this object supports rendering as an SVG element. This would
	 * enable vector based <code>PageBuilder</code>s to achieve better quality in
	 * the output results.
	 * 
	 * @return <code>true</code> if a call to <code>renderAsSvgElement()</code> is
	 * valid, <code>false</code> otherwise.
	 * 
	 * @see #renderAsSvgElement(Writer, PageRenderContext)
	 */
	public boolean isVectorGraphic();
	
	/**
	 * Renders this drawble as an SVG element. A typical implementation would create
	 * a <code>&lt;g&gt;</code> element with a z-index and left-top position 
	 * calculated as "pixel position" (using the given Page Render Context), 
	 * and optionally a transformation to size the contents according to the 
	 * parameters of the underlying MCF element (e.g. scaling a clipart). <br>
	 * If <code>isVectorGraphic()</code> for this object returns <code>false</code>,
	 * this method is free to (and should) throw an <code>UnsupportedOperationException</code>. 
	 * 
	 * @param writer Writer to write the SVG element and content to.
	 * @param context Current Page Render Context.
	 * @throws IOException If any I/O related problem occurs, e.g. reading a
	 * clipart file.
	 */
	public void renderAsSvgElement(Writer writer, PageRenderContext context) throws IOException;
	
	/**
	 * Renders this drawable as a bitmap according to the settings of the given
	 * Page Render Context. If attributions (border, shadow...) require a pixel-based
	 * drawing starting on another point than the point given by <code>getLeftMM()</code>
	 * and <code>getRightMM()</code>, the parameter <code>drawOffsetPixels</code>
	 * must be filled with the required drawing offset (in pixels).
	 * 
	 * @param context Current Page Render Context. 
	 * @param drawOffsetPixels Receives the required drawing offset, relative to
	 * the position indicated by <code>getLeftMM()</code> and <code>getRightMM()</code>,
	 * translated in pixels (using the Page Render Context).
	 * 
	 * @return The rendered bitmap.
	 * 
	 * @throws IOException If any I/O related problem occurs, e.g. reading an
	 * image file.
	 */
	public BufferedImage renderAsBitmap(PageRenderContext context, Point drawOffsetPixels) throws IOException;
	
	/**
	 * Returns the Z position of this drawable. This is mostly indicated by the
	 * underlying MCF elements (by their area). <br>
	 * This value is only used for bitmap based rendering; SVG elements rendered
	 * by <code>renderAsSvgElement()</code> must provide their own z-index attribute.
	 * 
	 * @return The Z Position of this drawable. The higher the value, the later
	 * will this drawable be drawn when the page is rendered.
	 */
	public int getZPosition();
	
	/**
	 * Returns the x-position of this drawable, relative to the left border of the
	 * current double page. This is mostly indicated by the underlying MCF elements (by their area). <br>
	 * This value is only used for bitmap based rendering; SVG elements rendered
	 * by <code>renderAsSvgElement()</code> must provide their own position attributes.
	 * 
	 * @return The x-Position of this drawable, in millimeters. 
	 */
	public float getLeftMM();
	
	/**
	 * Returns the y-position of this drawable, relative to the top border of the
	 * current double page. This is mostly indicated by the underlying MCF elements (by their area). <br>
	 * This value is only used for bitmap based rendering; SVG elements rendered
	 * by <code>renderAsSvgElement()</code> must provide their own position attributes.
	 * 
	 * @return The y-Position of this drawable, in millimeters. 
	 */
	public float getTopMM();

}
