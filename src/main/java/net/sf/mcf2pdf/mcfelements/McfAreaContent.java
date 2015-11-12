/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements;

/**
 * TODO comment
 */
public interface McfAreaContent {
	
	public static enum ContentType {
		IMAGE, IMAGEBACKGROUND, CLIPART, TEXT
	}
	
	public McfArea getArea();
	
	public ContentType getContentType();

}
