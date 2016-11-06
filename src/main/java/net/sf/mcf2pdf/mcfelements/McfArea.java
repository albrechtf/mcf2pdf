/*******************************************************************************
 * ${licenseText}
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements;

import java.awt.Color;
import java.util.regex.Pattern;

/**
 * TODO comment
 */
public interface McfArea {

	public final static Pattern IMAGEBACKGROUNDAREA = Pattern.compile("IMAGEBACKGROUNDAREA", Pattern.CASE_INSENSITIVE);

	public final static Pattern IMAGEAREA = Pattern.compile("IMAGEAREA", Pattern.CASE_INSENSITIVE);

	public final static Pattern TEXTAREA = Pattern.compile("FREETEXTAREA|textarea");

	public final static Pattern CLIPARTAREA = Pattern.compile("CLIPARTAREA", Pattern.CASE_INSENSITIVE);

	public McfPage getPage();

	public float getLeft();

	public float getTop();

	public float getHeight();

	public float getWidth();

	public float getRotation();

	public int getZPosition();

	public String getAreaType();

	public boolean isBorderEnabled();

	public float getBorderSize();

	public Color getBorderColor();

	public boolean isShadowEnabled();

	public int getShadowAngle();

	public int getShadowIntensity();

	public float getShadowDistance();

	public Color getBackgroundColor();

	public McfAreaContent getContent();

	public McfBorder getBorder();

}