/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.impl;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import net.sf.mcf2pdf.mcfelements.McfFotobook;
import net.sf.mcf2pdf.mcfelements.McfPage;


public class McfFotobookImpl implements McfFotobook {
	
	public File file;
	
	private int productType;
	
	private String productName;
	
	private String version;
	
	private String createdWithHPSVersion;
	
	private String programVersion;
	
	private String imageDir;
	
	private int normalPages;
	
	private int totalPages;
	
	private List<McfPage> pages = new Vector<McfPage>();
	
	public void addPage(McfPage page) {
		pages.add(page);
	}
	
	public List<? extends McfPage> getPages() {
		return Collections.unmodifiableList(pages);
	}
	
	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreatedWithHPSVersion() {
		return createdWithHPSVersion;
	}

	public void setCreatedWithHPSVersion(String createdWithHPSVersion) {
		this.createdWithHPSVersion = createdWithHPSVersion;
	}

	public String getProgramVersion() {
		return programVersion;
	}

	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}

	public String getImageDir() {
		return imageDir;
	}

	public void setImageDir(String imageDir) {
		this.imageDir = imageDir;
	}

	public int getNormalPages() {
		return normalPages;
	}

	public void setNormalPages(int normalPages) {
		this.normalPages = normalPages;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
