/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements;

/**
 * TODO comment
 */
public interface McfImage extends McfAreaContent {

	public String getParentChildRelationshipNature();

	public float getScale();

	public int getUseABK();

	public int getLeft();

	public int getTop();

	public String getFileNameMaster();

	public String getSafeContainerLocation();

	public String getFileName();
	
	public String getFadingFile();

}