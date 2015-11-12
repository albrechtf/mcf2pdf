/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.impl;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import net.sf.mcf2pdf.mcfelements.*;


public class McfPageImpl implements McfPage {
	
	private McfFotobook fotobook;
	
	private int pageNr;
	
	private String type;
	
	private List<McfArea> areas = new Vector<McfArea>();
	
	private List<McfBackground> backgrounds = new Vector<McfBackground>();
	
	public void addArea(McfArea area) {
		areas.add(area);
	}

	public List<? extends McfArea> getAreas() {
		return Collections.unmodifiableList(areas);
	}
	
	public void addBackground(McfBackground bg) {
		backgrounds.add(bg);
	}
	
	public List<McfBackground> getBackgrounds() {
		return Collections.unmodifiableList(backgrounds);
	}
	
	public McfFotobook getFotobook() {
		return fotobook;
	}

	public void setFotobook(McfFotobook fotobook) {
		this.fotobook = fotobook;
	}

	public int getPageNr() {
		return pageNr;
	}

	public void setPageNr(int pageNr) {
		this.pageNr = pageNr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
