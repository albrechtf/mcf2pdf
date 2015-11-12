/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.impl;

import net.sf.mcf2pdf.mcfelements.McfImage;

public class McfImageImpl extends AbstractMcfAreaContentImpl implements McfImage {
	
	private String parentChildRelationshipNature;
	
	private float scale;
	
	private int useABK;
	
	private int left;
	
	private int top;
	
	private String fileNameMaster;
	
	private String safeContainerLocation;
	
	private String fileName;
	
	private String fadingFile;
	
	@Override
	public ContentType getContentType() {
		return ContentType.IMAGE;
	}

	public String getParentChildRelationshipNature() {
		return parentChildRelationshipNature;
	}

	public void setParentChildRelationshipNature(
			String parentChildRelationshipNature) {
		this.parentChildRelationshipNature = parentChildRelationshipNature;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public int getUseABK() {
		return useABK;
	}

	public void setUseABK(int useABK) {
		this.useABK = useABK;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public String getFileNameMaster() {
		return fileNameMaster;
	}

	public void setFileNameMaster(String fileNameMaster) {
		this.fileNameMaster = fileNameMaster;
	}

	public String getSafeContainerLocation() {
		return safeContainerLocation;
	}

	public void setSafeContainerLocation(String safeContainerLocation) {
		this.safeContainerLocation = safeContainerLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFadingFile() {
		return fadingFile;
	}

	public void setFadingFile(String fadingFile) {
		this.fadingFile = fadingFile;
	}
	

}
