/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.impl;

import net.sf.mcf2pdf.mcfelements.McfImageBackground;

public class McfImageBackgroundImpl extends McfImageImpl implements McfImageBackground {
	
	private String backgroundPosition;
	
	@Override
	public ContentType getContentType() {
		return ContentType.IMAGEBACKGROUND;
	}
	
	public String getBackgroundPosition() {
		return backgroundPosition;
	}

	public void setBackgroundPosition(String backgroundPosition) {
		this.backgroundPosition = backgroundPosition;
	}

}
