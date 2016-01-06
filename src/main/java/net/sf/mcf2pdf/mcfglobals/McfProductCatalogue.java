/*******************************************************************************
 * ${licenseText}
 * All rights reserved. This file is made available under the terms of the 
 * Common Development and Distribution License (CDDL) v1.0 which accompanies 
 * this distribution, and is available at 
 * http://www.opensource.org/licenses/cddl1.txt     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfglobals;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import net.sf.mcf2pdf.mcfelements.util.DigesterUtil;
import net.sf.mcf2pdf.mcfglobals.impl.McfAlbumTypeImpl;
import net.sf.mcf2pdf.mcfglobals.impl.McfProductCatalogueImpl;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;



/**
 * TODO comment
 */
public abstract class McfProductCatalogue {
	
	public abstract McfAlbumType getAlbumType(String name);
	
	public abstract boolean isEmpty();
	
	/*
	public static McfProductCatalogue read(InputStream in) throws IOException, SAXException {
		Digester digester = new Digester();
		digester.addObjectCreate("fotobookdefinitions", McfProductCatalogueImpl.class);
		digester.addObjectCreate("fotobookdefinitions/album", McfAlbumTypeImpl.class);
		DigesterUtil.addSetProperties(digester, "fotobookdefinitions/album", getAlbumSpecialAttributes());
		DigesterUtil.addSetProperties(digester, "fotobookdefinitions/album/usablesize", getUsableSizeAttributes());
		digester.addCallMethod("fotobookdefinitions/album/spines/spine", "addSpine", 2, new String[] { Integer.class.getName(), Integer.class.getName() });
		digester.addCallParam("fotobookdefinitions/album/spines/spine", 0, "pages");
		digester.addCallParam("fotobookdefinitions/album/spines/spine", 1, "width");
		digester.addSetNext("fotobookdefinitions/album", "addAlbumType");
		
		return digester.parse(in);
	}
	*/
	public static McfProductCatalogue read(InputStream in) throws IOException, SAXException {
		Digester digester = new Digester();
		digester.addObjectCreate("description", McfProductCatalogueImpl.class);
		digester.addObjectCreate("description/product", McfAlbumTypeImpl.class);
		DigesterUtil.addSetProperties(digester, "description/product", getAlbumSpecialAttributes());
		DigesterUtil.addSetProperties(digester, "description/product/usablesize", getUsableSizeAttributes());
		digester.addCallMethod("description/product/spines/spine", "addSpine", 2, new String[] { Integer.class.getName(), Integer.class.getName() });
		digester.addCallParam("description/product/spines/spine", 0, "pages");
		digester.addCallParam("description/product/spines/spine", 1, "width");
		digester.addSetNext("description/product", "addAlbumType");
		
		return digester.parse(in);
	}
	private static List<String[]> getAlbumSpecialAttributes() {
		List<String[]> result = new Vector<String[]>();
		result.add(new String[] { "safetymargin", "safetyMargin" });
		result.add(new String[] { "bleedmargin", "bleedMargin" });
		result.add(new String[] { "normalpagehorizontalclamp", "normalPageHorizontalClamp" });
		result.add(new String[] { "coverextrahorizontal", "coverExtraHorizontal" });
		result.add(new String[] { "coverextravertical", "coverExtraVertical" });
		result.add(new String[] { "bleedmargincover", "bleedMarginCover" });
		return result;
	}
	
	private static List<String[]> getUsableSizeAttributes() {
		List<String[]> result = new Vector<String[]>();
		result.add(new String[] { "width", "usableWidth" });
		result.add(new String[] { "height", "usableHeight" });
		return result;
	}
	
}
