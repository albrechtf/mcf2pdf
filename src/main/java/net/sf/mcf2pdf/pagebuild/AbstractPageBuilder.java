/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.util.List;
import java.util.Vector;

public abstract class AbstractPageBuilder implements PageBuilder {
	
	private List<PageDrawable> pageContents = new Vector<PageDrawable>();
	
	@Override
	public void addDrawable(PageDrawable drawable) {
		pageContents.add(drawable);
	}
	
	protected final List<PageDrawable> getDrawables() {
		return pageContents;
	}

}
