/*******************************************************************************
 * ${licenseText}
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements;

import java.util.List;
import java.util.regex.Pattern;

/**
 * TODO comment
 */
public interface McfPage {

	public final static Pattern FULLCOVER = Pattern.compile("FULLCOVER|fullcover");

	public final static Pattern CONTENT = Pattern.compile("CONTENT|normalpage");

	public final static Pattern EMPTY = Pattern.compile("EMPTY|emptypage");

	public final static Pattern SPINE = Pattern.compile("SPINE|spine");

	public McfFotobook getFotobook();

	public List<? extends McfArea> getAreas();

	public List<? extends McfBackground> getBackgrounds();

	public int getPageNr();

	public String getType();

}