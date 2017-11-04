/*******************************************************************************
 * ${licenseText}
 *******************************************************************************/
package net.sf.mcf2pdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import net.sf.mcf2pdf.mcfelements.util.ImageUtil;
import net.sf.mcf2pdf.mcfelements.util.PdfUtil;


/**
 * Main entry point of the mcf2pdf application. Creates an
 * <code>Mcf2FoConverter</code> object with the settings passed on command line,
 * and renders the given input file to XSL-FO. If PDF output is requested
 * (the default), the XSL-FO is converted to PDF using Apache FOP. The result
 * (PDF or XSL-FO) is then written to given output file, which can be STDOUT
 * when a dash (-) is passed.
 */
public class Main {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();

		Option o = OptionBuilder.hasArg().isRequired().withDescription("Installation location of My CEWE Photobook. REQUIRED.").create('i');
		options.addOption(o);
		options.addOption("h", false, "Prints this help and exits.");
		options.addOption("t", true, "Location of MCF temporary files.");
		options.addOption("w", true, "Location for temporary images generated during conversion.");
		options.addOption("r", true, "Sets the resolution to use for page rendering, in DPI. Default is 150.");
		options.addOption("n", true, "Sets the page number to render up to. Default renders all pages.");
		options.addOption("b", false, "Prevents rendering of binding between double pages.");
		options.addOption("x", false, "Generates only XSL-FO content instead of PDF content.");
		options.addOption("q", false, "Quiet mode - only errors are logged.");
		options.addOption("d", false, "Enables debugging logging output.");

		CommandLine cl;
		try {
			CommandLineParser parser = new PosixParser();
			cl = parser.parse(options, args);
		}
		catch (ParseException pe) {
			printUsage(options, pe);
			System.exit(3);
			return;
		}

		if (cl.hasOption("h")) {
			printUsage(options, null);
			return;
		}

		if (cl.getArgs().length != 2) {
			printUsage(options, new ParseException("INFILE and OUTFILE must be specified. Arguments were: " + cl.getArgList()));
			System.exit(3);
			return;
		}

		File installDir = new File(cl.getOptionValue("i"));
		if (!installDir.isDirectory()) {
			printUsage(options, new ParseException("Specified installation directory does not exist."));
			System.exit(3);
			return;
		}

		File tempDir = null;
		String sTempDir = cl.getOptionValue("t");
		if (sTempDir == null) {
			tempDir = new File(new File(System.getProperty("user.home")), ".mcf");
			if (!tempDir.isDirectory()) {
				printUsage(options, new ParseException("MCF temporary location not specified and default location " + tempDir + " does not exist."));
				System.exit(3);
				return;
			}
		}
		else {
			tempDir = new File(sTempDir);
			if (!tempDir.isDirectory()) {
				printUsage(options, new ParseException("Specified temporary location does not exist."));
				System.exit(3);
				return;
			}
		}

		File mcfFile = new File(cl.getArgs()[0]);
		if (!mcfFile.isFile()) {
			printUsage(options, new ParseException("MCF input file does not exist."));
			System.exit(3);
			return;
		}
		mcfFile = mcfFile.getAbsoluteFile();

		File tempImages = new File(new File(System.getProperty("user.home")), ".mcf2pdf");
		if (cl.hasOption("w")) {
			tempImages = new File(cl.getOptionValue("w"));
			if (!tempImages.mkdirs() && !tempImages.isDirectory()) {
				printUsage(options, new ParseException("Specified working dir does not exist and could not be created."));
				System.exit(3);
				return;
			}
		}

		int dpi = 150;
		if (cl.hasOption("r")) {
			try {
				dpi = Integer.valueOf(cl.getOptionValue("r")).intValue();
				if (dpi < 30 || dpi > 600)
					throw new IllegalArgumentException();
			}
			catch (Exception e) {
				printUsage(options, new ParseException("Parameter for option -r must be an integer between 30 and 600."));
			}
		}

		int maxPageNo = -1;
		if (cl.hasOption("n")) {
			try {
				maxPageNo = Integer.valueOf(cl.getOptionValue("n")).intValue();
				if (maxPageNo < 0)
					throw new IllegalArgumentException();
			}
			catch (Exception e) {
				printUsage(options, new ParseException("Parameter for option -n must be an integer >= 0."));
			}
		}

		boolean binding = true;
		if (cl.hasOption("b")) {
			binding = false;
		}

		OutputStream finalOut;
		if (cl.getArgs()[1].equals("-"))
			finalOut = System.out;
		else {
			try {
				finalOut = new FileOutputStream(cl.getArgs()[1]);
			}
			catch (IOException e) {
				printUsage(options, new ParseException("Output file could not be created."));
				System.exit(3);
				return;
			}
		}

		// configure logging, if no system property is present
		if (System.getProperty("log4j.configuration") == null) {
			PropertyConfigurator.configure(Main.class.getClassLoader().getResource("log4j.properties"));

			Logger.getRootLogger().setLevel(Level.INFO);
			if (cl.hasOption("q"))
				Logger.getRootLogger().setLevel(Level.ERROR);
			if (cl.hasOption("d"))
				Logger.getRootLogger().setLevel(Level.DEBUG);
		}

		// start conversion to XSL-FO
		// if -x is specified, this is the only thing we do
		OutputStream xslFoOut;
		if (cl.hasOption("x"))
			xslFoOut = finalOut;
		else
			xslFoOut = new ByteArrayOutputStream();

		Log log = LogFactory.getLog(Main.class);

		// TODO this is a bad pattern, fix it.
		ImageUtil.init(installDir);

		try {
			new Mcf2FoConverter(installDir, tempDir, tempImages).convert(
					mcfFile, xslFoOut, dpi, binding, maxPageNo);
			xslFoOut.flush();

			if (!cl.hasOption("x")) {
				// convert to PDF
				log.debug("Converting XSL-FO data to PDF");
				byte[] data = ((ByteArrayOutputStream)xslFoOut).toByteArray();
				PdfUtil.convertFO2PDF(new ByteArrayInputStream(data), finalOut, dpi);
				finalOut.flush();
			}
		}
		catch (Exception e) {
			log.error("An exception has occured", e);
			System.exit(1);
			return;
		}
		finally {
			if (finalOut instanceof FileOutputStream) {
				try { finalOut.close(); } catch (Exception e) { }
			}
		}
	}

	private static void printUsage(Options options, ParseException pe) {
		if (pe != null)
			System.err.println("ERROR: " + pe.getMessage());
		System.out.println();
		System.out.println("mcf2pdf My CEWE Photobook to PDF converter");
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("mcf2pdf <options> INFILE OUTFILE", "Options are:", options,
				"If -t is not specified, <USER_HOME>/.mcf is used.\n" +
						"If -w is not specified, <USER_HOME>/.mcf2pdf is created and used.\n" +
				"If you specify a dash (-) as OUTFILE, resulting content will be written to STDOUT. Notice that, in that case, temporary image files will be kept in specified image working directory. Notice also that you should add option -q in this case to avoid logging output.");
	}
}
