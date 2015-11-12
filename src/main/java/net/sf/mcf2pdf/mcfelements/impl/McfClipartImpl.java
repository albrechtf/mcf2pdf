/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.impl;

import net.sf.mcf2pdf.mcfelements.McfClipart;

public class McfClipartImpl extends AbstractMcfAreaContentImpl implements McfClipart {
	
	private String uniqueName;

	@Override
	public ContentType getContentType() {
		return ContentType.CLIPART;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}


}
