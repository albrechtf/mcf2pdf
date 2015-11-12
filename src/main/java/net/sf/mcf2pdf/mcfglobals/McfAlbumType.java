/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfglobals;

/**
 * TODO comment
 */
public interface McfAlbumType {

	public String getName();

	public int getSafetyMargin();

	public int getBleedMargin();

	public int getNormalPageHorizontalClamp();

	public int getUsableWidth();

	public int getUsableHeight();

	public int getBleedMarginCover();

	public int getCoverExtraVertical();

	public int getCoverExtraHorizontal();

	public int getSpineWidth(int normalPageCount);

}