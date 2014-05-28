package cz.sio2.crowler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import cz.sio2.crowler.configurations.json.JsonConfiguration;
import cz.sio2.crowler.connectors.FileJenaConnector;
import cz.sio2.crowler.connectors.SesameJenaConnector;
import cz.sio2.crowler.model.ConfigurationFactory;

public class Runner {

	public static void main(String[] args) {
		String jarDir = teeOutputs(args);

		System.out.println("Working Directory = "
				+ System.getProperty("user.dir"));

		System.out.println("Jar directory = " + jarDir);

		// try {
		if (!Arrays.asList(new String[] { "file", "sesame" }).contains(args[1])) {
			System.out
					.println("Usage: Runner <CONFIGURATION_CLASS> (file <dirname>) | (sesame <serverurl> <repositoryid>) [json_conf_file]>");
			System.exit(0);
		}
		// } catch (ArrayIndexOutOfBoundsException ex) {
		// System.out
		// .println("Usage: Runner <CONFIGURATION_CLASS> (file filename) | (sesame serverurl repositoryid)>");
		// System.exit(0);
		// }

		// TODO really need parser for arguments.. it's starting to get messy

		JenaConnector connector = null;

		if ("file".equals(args[1])) {
			connector = new FileJenaConnector(new File(jarDir, args[2]), false);
		} else if ("sesame".equals(args[1])) {
			XTrustProvider.install();
			final SesameJenaConnector cx = new SesameJenaConnector();
			cx.setServerUrl(args[2]);
			cx.setRepositoryId(args[3]);

			// cx.setServerUrl("http://onto.mondis.cz/openrdf-sesame");//args[1]);
			// cx.setRepositoryId("mondis-webdata");//args[2]);//id+"-"+Calendar.getInstance());
			// cx.setServerUrl("https://dev.sio2.cz/openrdf-sesame");//args[1]);
			// cx.setRepositoryId("monumnet-webdata");//args[2]);//id+"-"+Calendar.getInstance());
			connector = cx;
		}
		try {
			final ConfigurationFactory conf_fact = (ConfigurationFactory) Class
					.forName(args[0]).newInstance();
			// if we are in selenium, we need to load scripts
			if (args[0].contains("JsonConfiguration")) {
				((JsonConfiguration) conf_fact).setScenario(new File(jarDir, "scenario.json"));
			}
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
	private static String teeOutputs(String[] args) {
		// jarDir = System.getProperty("user.dir");
		// URL url = Runner.class.getProtectionDomain().getCodeSource()
		// .getLocation();
		// try {
		// jarDir = new File(url.toURI()).getParentFile().getPath();
		// } catch (URISyntaxException e2) {
		// jarDir = new File(url.getPath()).getParentFile().getPath();
		// }

		String jarDir = args[args.length - 1];

		try {
			System.setOut(new PrintStream(new File(jarDir, "./log.out")));
			System.setErr(new PrintStream(new File(jarDir, "./log.err")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return jarDir;
	}
}
