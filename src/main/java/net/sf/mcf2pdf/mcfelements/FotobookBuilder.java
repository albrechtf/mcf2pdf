/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements;

import java.io.File;
import java.io.IOException;

import net.sf.mcf2pdf.mcfelements.impl.DigesterConfiguratorImpl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;


/**
 * Entry point to create an MCF DOM from a given MCF input file.
 */
public final class FotobookBuilder {
	
	private DigesterConfigurator configurator;
	
	/**
	 * Creates a new MCF DOM builder with default settings.
	 */
	public FotobookBuilder() {
		this(new DigesterConfiguratorImpl());
	}
	
	/**
	 * Creates a new MCF DOM builder which uses the given configurator to configure
	 * the XML Digester to use. This way, you can use your own API or add some
	 * preprocessing to the parsing process.
	 * 
	 * @param configurator Configurator to use to configure the internal Digester
	 * object.
	 */
	public FotobookBuilder(DigesterConfigurator configurator) {
		this.configurator = configurator;
	}
	
	/**
	 * Reads the given MCF input file and creates an MCF DOM reflecting the
	 * relevant parts of the content.
	 *  
	 * @param file MCF input file.
	 * 
	 * @return A Fotobook object reflecting the contents of the file.
	 * 
	 * @throws IOException If any I/O related problem occurs reading the file.
	 * @throws SAXException If any XML related problem occurs reading the file.
	 */
	public McfFotobook readFotobook(File file) throws IOException, SAXException {
		Digester digester = new Digester();
		configurator.configureDigester(digester, file);
		McfFotobook fb = digester.parse(file);
		
		// try to set file on it
		try {
			BeanUtils.setProperty(fb, "file", file);
		}
		catch (Exception e) {
			// ignore - digester configurator will (hopefully) do it anyhow
		}
		
		// if this is thrown, your McfFotobook implementation has no setFile(),
		// and your configurator has also not set it. 
		if (!file.equals(fb.getFile()))
			throw new IllegalStateException("File could not be set on photobook. Please check used DigesterConfigurator.");
		
		return fb;
	}
	
}
