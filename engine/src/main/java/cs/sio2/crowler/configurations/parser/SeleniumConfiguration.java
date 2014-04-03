package cs.sio2.crowler.configurations.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.openjena.atlas.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
	String SOURCE_URL = "";//"http://localhost:8888/";
	String ID_PREFIX = "";
	
	// scripts from Selenium to be parsed and followed
	List<File> scripts;

	public void setStcripts(List<File> scripts) {
		// TODO check - make copy
		this.scripts = new ArrayList<File>(scripts);
	}

	@Override
	public Configuration getConfiguration(final Map<String, String> properties) {
		final Configuration conf = new Configuration();
		
		Document doc = null;
		XPath xpath = null;
		XPathExpression expr = null;
		
		// parse script
		try {
			// init
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.parse(scripts.get(0));
			XPathFactory xPathfactory = XPathFactory.newInstance();
			xpath = xPathfactory.newXPath();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			// get an url ;)
			// <link rel="selenium.base" href="localhost:8888" />
			expr = xpath.compile("//link[@rel='selenium.base']/@href");
			SOURCE_URL = (String)expr.evaluate(doc, XPathConstants.STRING);
			logger.info("source url: " + SOURCE_URL);
		
			// parse owlSetSchemas
			//<tr> <td>owlSetSchemas</td> <td></td> <td>http://kub1x.org/dip/gen/</td> </tr>
			expr = xpath.compile("//tbody/tr/td[text()='owlSetSchemas']/../td[3]");
			ID_PREFIX = (String)expr.evaluate(doc, XPathConstants.STRING); 
			conf.setBaseOntoPrefix(ID_PREFIX);
			logger.info("id prefix: " + ID_PREFIX);
		
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		// Set technical details //
		//conf.setSchemas(new String[] { SeleniumModel.idPrefix });
		conf.setSchemas(new String[] { ID_PREFIX });
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
		//conf.setBaseOntoPrefix(SeleniumModel.idPrefix);

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
