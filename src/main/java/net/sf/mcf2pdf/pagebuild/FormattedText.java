/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Color;

public class FormattedText {
	
	private String text;
	
	private boolean bold;
	private boolean italic;
	private boolean underline;
	
	private Color textColor;
	
	private String fontFamily;
	private float fontSize;

	public FormattedText(String text, boolean bold, boolean italic,
			boolean underline, Color textColor, String fontFamily, float fontSize) {
		this.text = text;
		this.bold = bold;
		this.italic = italic;
		this.underline = underline;
		this.textColor = textColor;
		this.fontFamily = fontFamily;
		this.fontSize = fontSize;
	}

	public String getText() {
		return text;
	}

	public boolean isBold() {
		return bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public boolean isUnderline() {
		return underline;
	}

	public Color getTextColor() {
		return textColor;
	}
	
	public String getFontFamily() {
		return fontFamily;
	}

	public float getFontSize() {
		return fontSize;
	}

}
