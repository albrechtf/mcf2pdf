/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements;

/**
 * TODO comment
 */
public interface McfBackground {
	
	public McfPage getPage();

	public String getTemplateName();

	public int getType();

	public int getLayout();

}