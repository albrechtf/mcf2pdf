/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.util;

import java.util.List;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.SetPropertiesRule;

/**
 * TODO comment
 */
public final class DigesterUtil {
	
	private DigesterUtil() {
	}
	
	public static void addSetProperties(Digester digester, String pattern,
			List<String[]> specialAttributes) {
		String[] attrNames = new String[specialAttributes.size()];
		String[] propNames = new String[specialAttributes.size()];
		
		for (int i = 0; i < specialAttributes.size(); i++) {
			attrNames[i] = specialAttributes.get(i)[0];
			propNames[i] = specialAttributes.get(i)[1];
		}
		
		SetPropertiesRule rule = new SetPropertiesRule(attrNames, propNames);
		rule.setIgnoreMissingProperty(true);
		digester.addRule(pattern, rule);
	}
	
 

}
