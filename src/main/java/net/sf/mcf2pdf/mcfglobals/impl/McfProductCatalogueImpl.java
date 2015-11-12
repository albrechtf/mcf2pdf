/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfglobals.impl;

import java.util.List;
import java.util.Vector;

import net.sf.mcf2pdf.mcfglobals.McfAlbumType;
import net.sf.mcf2pdf.mcfglobals.McfProductCatalogue;


public class McfProductCatalogueImpl extends McfProductCatalogue {
	
	private List<McfAlbumType> albumTypes = new Vector<McfAlbumType>();
	
	public McfProductCatalogueImpl() {
	}
	
	public void addAlbumType(McfAlbumType albumType) {
		albumTypes.add(albumType);
	}
	
	@Override
	public McfAlbumType getAlbumType(String name) {
		for (McfAlbumType a : albumTypes) {
			if (name.equals(a.getName()))
				return a;
		}
		
		return null;
	}
	
	@Override
	public boolean isEmpty() {
		return albumTypes.isEmpty();
	}

}
