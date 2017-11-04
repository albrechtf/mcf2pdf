package net.sf.mcf2pdf.mcfglobals;

import java.io.File;

import net.sf.mcf2pdf.mcfconfig.Fading;

public class McfFotoFrame {
	private File clipart;
	private File fading;
	private Fading config;
	
	public McfFotoFrame(File clipart, File fading, Fading config) {
		this.clipart = clipart;
		this.fading = fading;
		this.config = config;
	}

	public File getClipart() {
		return clipart;
	}

	public File getFading() {
		return fading;
	}

	public Fading getConfig() {
		return config;
	}
	
	
}
