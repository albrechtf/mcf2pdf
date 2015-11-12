/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.impl;

import net.sf.mcf2pdf.mcfelements.McfText;

public class McfTextImpl extends AbstractMcfAreaContentImpl implements McfText {
	
	private boolean isSpineText;
	
	private String htmlContent;
	
	private float verticalIndentMargin;
	
	private float indentMargin;
	
	private int backgroundColorAlpha;
	

	@Override
	public ContentType getContentType() {
		return ContentType.TEXT;
	}

	@Override
	public boolean isSpineText() {
		return isSpineText;
	}

	public void setSpineText(boolean isSpineText) {
		this.isSpineText = isSpineText;
	}
	
	@Override
	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	@Override
	public float getVerticalIndentMargin() {
		return verticalIndentMargin;
	}

	public void setVerticalIndentMargin(float verticalIndentMargin) {
		this.verticalIndentMargin = verticalIndentMargin;
	}

	@Override
	public float getIndentMargin() {
		return indentMargin;
	}

	public void setIndentMargin(float indentMargin) {
		this.indentMargin = indentMargin;
	}

	@Override
	public int getBackgroundColorAlpha() {
		return backgroundColorAlpha;
	}

	public void setBackgroundColorAlpha(int backgroundColorAlpha) {
		this.backgroundColorAlpha = backgroundColorAlpha;
	}

}
