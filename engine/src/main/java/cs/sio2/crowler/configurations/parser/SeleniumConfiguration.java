package cs.sio2.crowler.configurations.parser;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.sio2.crowler.Factory;
import cz.sio2.crowler.FullCrawler;
import cz.sio2.crowler.Utils;
import cz.sio2.crowler.model.ClassSpec;
import cz.sio2.crowler.model.Configuration;
import cz.sio2.crowler.model.ConfigurationFactory;
import cz.sio2.crowler.model.EnumeratedNextPageResolver;

public class SeleniumConfiguration implements ConfigurationFactory {

	private Logger logger = LoggerFactory.getLogger(SeleniumConfiguration.class
			.getName());

	// final String SOURCE_URL = "http://www.inventati.org/kub1x/t/";
	final String SOURCE_URL = "http://localhost:8888/";

	@Override
	public Configuration getConfiguration(final Map<String, String> properties) {
		final Configuration conf = new Configuration();

		logger.error("selenium script parameter: " + properties.get("script"));
		// System.out.println("selenium script parameter: " +
		// properties.get("script"));

		// Set technical details //
		conf.setSchemas(new String[] { SeleniumModel.idPrefix });
		conf.setEncoding("iso-8859-2");
		conf.setLang("cs");
		conf.setPublisher(SOURCE_URL);

		// Build and set source URL list
		// SimpleDateFormat f = new SimpleDateFormat();
		// f.applyPattern("dd.MM.yyyy");
		// String date = f.format(Calendar.getInstance().getTime());
		conf.setNextPageResolver(new EnumeratedNextPageResolver(SOURCE_URL));

		// Set ID and IRI
		conf.setId(Utils.getFullId("person"));
		conf.setBaseOntoPrefix(SeleniumModel.idPrefix);

		// Get base object (for each tr in table)
		ClassSpec chObject = Factory.createClassSpec(SeleniumModel.personRecord
				.getURI());
		conf.addInitialDefinition(Factory.createInitialDefinition(chObject,
				Factory.createJSoupSelector("table tr")));

		// Get it's first name spec
		chObject.addSpec(true, Factory.createDPSpec(
				Factory.createJSoupSelector("td:eq(0)"),
				SeleniumModel.hasFirstName.getURI()));
		chObject.addSpec(true, Factory.createDPSpec(
				Factory.createJSoupSelector("td:eq(1)"),
				SeleniumModel.hasFamilyName.getURI()));
		chObject.addSpec(Factory.createDPSpec(
				Factory.createJSoupSelector("td:eq(2)"),
				SeleniumModel.hasPhoneNumber.getURI()));

		return conf;
	}

}
