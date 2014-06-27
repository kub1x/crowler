package cz.sio2.crowler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

import cz.sio2.crowler.configurations.json.JsonConfiguration;
import cz.sio2.crowler.connectors.FileJenaConnector;
import cz.sio2.crowler.connectors.SesameJenaConnector;
import cz.sio2.crowler.model.ConfigurationFactory;

public class Runner {

	// @Option(name="--sesameUrl")
	// private String sesameUrl = null;
	//
	// @Option(name="--repositoryId")
	// private String repositoryId = null;

	@Option(name = "--confClass", usage="class of configuration to be used")
	private String confClass = null;

	@Option(name = "--file", usage="print output as files into directory specified by this argument (currently default, required)")
	private String file = null;

	@Option(name = "--jarDir", usage="pass directory of the crowler.jar jarball (currently required)")
	private String jarDir = null;
	
	@Option(name="--scenario", usage="path to xml scenario (currently required)")
	private String scenario = null;
	
	@Option(name="--selenium", usage="flag if selenium crowling is to be used (currently default)")
	private boolean selenium = true;

	public static void main(String[] args) {
		new Runner().doMain(args);
	}

	public void doMain(String[] args) {
		// String jarDir = teeOutputs(args);

		System.out.println("Working Directory = "
				+ System.getProperty("user.dir"));

		System.out.println("Jar directory = " + jarDir);

		// Parsing console arguments
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(160); // Wider console output
		try {
            // parse the arguments.
            parser.parseArgument(args);

            // you can parse additional arguments if you want.
            // parser.parseArgument("more","args");

            // after parsing arguments, you should check
            // if enough arguments are given.
			// if( arguments.isEmpty() )
			// throw new CmdLineException(parser,"No argument is given");

        } catch( CmdLineException e ) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("usage: java -jar crowler.jar [args - see below]");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            return;
        }
		
		
		// //try {
		// if (!Arrays.asList(new String[] { "file", "sesame"
		// }).contains(args[1])) {
		// System.out
		// .println("Usage: Runner <CONFIGURATION_CLASS> (file <dirname>) | (sesame <serverurl> <repositoryid>) [json_conf_file]>");
		// System.exit(0);
		// }
		// //} catch (ArrayIndexOutOfBoundsException ex) {
		// //System.out
		// //.println("Usage: Runner <CONFIGURATION_CLASS> (file filename) | (sesame serverurl repositoryid)>");
		// //System.exit(0);
		// //}

		JenaConnector connector = null;

		if (file != null) {
			connector = new FileJenaConnector(new File(jarDir, file), false);
		}
		// if (sesame != null) {
		// XTrustProvider.install();
		// final SesameJenaConnector cx = new SesameJenaConnector();
		// cx.setServerUrl(sesameUrl);
		// cx.setRepositoryId(repositoryId);
		//
		// //cx.setServerUrl("http://onto.mondis.cz/openrdf-sesame");//args[1]);
		// //cx.setRepositoryId("mondis-webdata");//args[2]);//id+"-"+Calendar.getInstance());
		// //cx.setServerUrl("https://dev.sio2.cz/openrdf-sesame");//args[1]);
		// //cx.setRepositoryId("monumnet-webdata");//args[2]);//id+"-"+Calendar.getInstance());
		// connector = cx;
		// }
		try {
			final ConfigurationFactory conf_fact = (ConfigurationFactory) Class
					.forName(confClass).newInstance();
			// // if we are in selenium, we need to load scripts
			// if (confClass.contains("JsonConfiguration")) {
			// ((JsonConfiguration) conf_fact).setScenario(new File(jarDir,
			// "scenario.json"));
			// }
			new FullCrawler(connector).run(conf_fact
					.getConfiguration(new HashMap(System.getProperties())));
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * for obtaining the path of jar-ball (that contains this program) from
	 * inside see:
	 * http://stackoverflow.com/questions/320542/how-to-get-the-path-
	 * of-a-running-jar-file#12733172
	 * https://weblogs.java.net/blog/kohsuke/archive/2007/04/how_to_convert.html
	 * 
	 * @return
	 */
	// private static String teeOutputs(String[] args) {
	// // jarDir = System.getProperty("user.dir");
	// // URL url = Runner.class.getProtectionDomain().getCodeSource()
	// // .getLocation();
	// // try {
	// // jarDir = new File(url.toURI()).getParentFile().getPath();
	// // } catch (URISyntaxException e2) {
	// // jarDir = new File(url.getPath()).getParentFile().getPath();
	// // }
	//
	// String jarDir = args[args.length - 1];
	//
	// try {
	// System.setOut(new PrintStream(new File(jarDir, "./log.out")));
	// System.setErr(new PrintStream(new File(jarDir, "./log.err")));
	// } catch (FileNotFoundException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// return jarDir;
	// }
}
