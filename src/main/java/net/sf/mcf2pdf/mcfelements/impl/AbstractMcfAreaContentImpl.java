/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.impl;

import net.sf.mcf2pdf.mcfelements.McfArea;
import net.sf.mcf2pdf.mcfelements.McfAreaContent;

public abstract class AbstractMcfAreaContentImpl implements McfAreaContent {
	
	private McfArea area;

	public McfArea getArea() {
		return area;
	}

	public void setArea(McfArea area) {
		this.area = area;
	}


}
