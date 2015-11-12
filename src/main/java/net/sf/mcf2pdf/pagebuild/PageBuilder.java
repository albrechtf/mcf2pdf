/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.io.IOException;

import net.sf.mcf2pdf.mcfelements.util.XslFoDocumentBuilder;


/**
 * Interface for page builders. A page builder is responsible for rendering a
 * "single" page (single from its point of view. In the context of mcf2pdf, one
 * page will always reflect two pages, but this is not managed by the PageBuilder).
 * The page builder can receive any number of <code>PageDrawable</code>s which
 * must be rendered to the page, preserving their Z ordering. The page must then
 * be added to a given <code>XslFoDocumentBuilder</code>.
 *
 */
public interface PageBuilder {
	
	/**
	 * Adds a single drawable to the page.
	 * 
	 * @param drawable Drawable to add.
	 */
	public void addDrawable(PageDrawable drawable);
	
	/**
	 * Adds this page to the given document builder. Normally, now the rendering
	 * of all drawables is performed, and then one "page" object is added to the
	 * builder.
	 *  
	 * @param docBuilder Document Builder to add a page element to.
	 * 
	 * @throws IOException If any I/O related problem occurs, e.g. when reading
	 * an image file during rendering.
	 */
	public void addToDocumentBuilder(XslFoDocumentBuilder docBuilder) throws IOException;

}
