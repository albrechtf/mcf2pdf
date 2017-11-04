package net.sf.mcf2pdf.mcfconfig;

public class Fading {
	private String file;
	private String designElementType;
	private double ratio;
	private double keepAspectRatio;
	
	private Clipart clipart;
	private Fotoarea fotoarea;
	
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
	public double getKeepAspectRatio() {
		return keepAspectRatio;
	}
	public void setKeepAspectRatio(double keepAspectRatio) {
		this.keepAspectRatio = keepAspectRatio;
	}
	public Clipart getClipart() {
		return clipart;
	}
	public void setClipart(Clipart clipart) {
		this.clipart = clipart;
	}
	public Fotoarea getFotoarea() {
		return fotoarea;
	}
	public void setFotoarea(Fotoarea fotoarea) {
		this.fotoarea = fotoarea;
	}
}
