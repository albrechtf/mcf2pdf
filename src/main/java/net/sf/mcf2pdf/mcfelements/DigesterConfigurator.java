/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester3.Digester;

/**
 * Interface for objects being able to configure a Digester object to
 * be ready to parse the given MCF file.
 */
public interface DigesterConfigurator {
	
	/**
	 * Configures the given Digester to parse the given MCF file and create
	 * an MCF DOM with an <code>McfFotobook</code> as root object. <br>
	 * As the root <code>McfFotobook</code> object has a <code>getFile()</code>
	 * method, this property must somehow be set on the root object. A normal way
	 * would be to provide a setter for this property in the used <code>McfFotobook</code>
	 * implementing class. This property is then set by the <code>FotobookBuilder</code>
	 * on the resulting <code>McfFotobook</code> object after parsing the MCF file, 
	 * using BeanUtils API. <br> 
	 * If, for some reason, your chosen DOM implementation does not offer such a 
	 * setter for the file property, you can use the File object here to somehow
	 * inject it into your root object class.
	 *  
	 * @param digester Digester object to configure.
	 * @param mcfFile MCF file which is going to be parsed.
	 * 
	 * @throws IOException If any I/O related problem occurs, e.g. when analysing
	 * the file is required but fails.
	 */
	public void configureDigester(Digester digester, File mcfFile) throws IOException;

}
