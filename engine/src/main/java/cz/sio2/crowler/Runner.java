package cz.sio2.crowler;

import cz.sio2.crowler.configurations.parser.SeleniumConfiguration;
import cz.sio2.crowler.connectors.FileJenaConnector;
import cz.sio2.crowler.connectors.SesameJenaConnector;
import cz.sio2.crowler.model.ConfigurationFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Runner {

	public static void main(String[] args) {

		// try {
		if (!Arrays.asList(new String[] { "file", "sesame" }).contains(args[1])) {
			System.out
					.println("Usage: Runner <CONFIGURATION_CLASS> (file dirname) | (sesame serverurl repositoryid) [selenium_conf_file]>");
			System.exit(0);
		}
		// } catch (ArrayIndexOutOfBoundsException ex) {
		// System.out
		// .println("Usage: Runner <CONFIGURATION_CLASS> (file filename) | (sesame serverurl repositoryid)>");
		// System.exit(0);
		// }

		JenaConnector connector = null;

		if ("file".equals(args[1])) {
			connector = new FileJenaConnector(new File(args[2]), false);
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
			if (args[0].contains("SeleniumConfiguration")) {
				List<File> scripts = new ArrayList<File>();
				//TODO starting from 2 for "file" option.. for "sesame" it will be 4
				for (int i = 3; i < args.length; i++) {
					scripts.add(new File(args[i]));
				}
				((SeleniumConfiguration) conf_fact).setStcripts(scripts);
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
}
