/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.impl;

import net.sf.mcf2pdf.mcfelements.McfBackground;
import net.sf.mcf2pdf.mcfelements.McfPage;

public class McfBackgroundImpl implements McfBackground {
	
	private McfPage page;
	
	private String templateName;
	
	private int type;
	
	private int layout;
	
	public McfPage getPage() {
		return page;
	}

	public void setPage(McfPage page) {
		this.page = page;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLayout() {
		return layout;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

}
