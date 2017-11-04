package net.sf.mcf2pdf.mcfconfig;

public class Clipart {
	private String file;
	private String designElementType; 
	private double ratio;
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getDesignElementType() {
		return designElementType;
	}
	public void setDesignElementType(String designElementType) {
		this.designElementType = designElementType;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
}