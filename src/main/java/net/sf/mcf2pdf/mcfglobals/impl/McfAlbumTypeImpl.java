/*******************************************************************************
 * ${licenseText}
 * All rights reserved. This file is made available under the terms of the 
 * Common Development and Distribution License (CDDL) v1.0 which accompanies 
 * this distribution, and is available at 
 * http://www.opensource.org/licenses/cddl1.txt     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfglobals.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.mcf2pdf.mcfglobals.McfAlbumType;


public class McfAlbumTypeImpl implements McfAlbumType {
	
	private String name;
	
	private int safetyMargin;
	
	private int bleedMargin;
	
	private int normalPageHorizontalClamp;
	
	private int usableWidth;
	
	private int usableHeight;
	
	private int bleedMarginCover;
	
	private int coverExtraVertical;
	
	private int coverExtraHorizontal;
	
	private Map<Integer, Integer> spineWidths = new HashMap<Integer, Integer>();
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getSafetyMargin() {
		return safetyMargin;
	}

	@Override
	public int getBleedMargin() {
		return bleedMargin;
	}


	@Override
	public int getNormalPageHorizontalClamp() {
		return normalPageHorizontalClamp;
	}

	@Override
	public int getUsableWidth() {
		return usableWidth;
	}

	@Override
	public int getUsableHeight() {
		return usableHeight;
	}
	
	@Override
	public int getBleedMarginCover() {
		return bleedMarginCover;
	}

	@Override
	public int getCoverExtraVertical() {
		return coverExtraVertical;
	}

	@Override
	public int getCoverExtraHorizontal() {
		return coverExtraHorizontal;
	}
	
	@Override
	public int getSpineWidth(int normalPageCount) {
		Integer key = Integer.valueOf(normalPageCount);
		if (!spineWidths.containsKey(key))
			return 0;
		
		return spineWidths.get(key).intValue();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSafetyMargin(int safetyMargin) {
		this.safetyMargin = safetyMargin;
	}

	public void setBleedMargin(int bleedMargin) {
		this.bleedMargin = bleedMargin;
	}

	public void setNormalPageHorizontalClamp(int normalPageHorizontalClamp) {
		this.normalPageHorizontalClamp = normalPageHorizontalClamp;
	}

	public void setUsableWidth(int usableWidth) {
		this.usableWidth = usableWidth;
	}

	public void setUsableHeight(int usableHeight) {
		this.usableHeight = usableHeight;
	}

	public void setBleedMarginCover(int bleedMarginCover) {
		this.bleedMarginCover = bleedMarginCover;
	}

	public void setCoverExtraVertical(int coverExtraVertical) {
		this.coverExtraVertical = coverExtraVertical;
	}

	public void setCoverExtraHorizontal(int coverExtraHorizontal) {
		this.coverExtraHorizontal = coverExtraHorizontal;
	}
	
	public void addSpine(int pageCount, int width) {
		spineWidths.put(pageCount, width);
	}
	
}
