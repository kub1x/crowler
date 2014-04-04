package cz.sio2.crowler.configurations.parser;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
	String SOURCE_URL = "";// "http://localhost:8888/";

	// scripts from Selenium to be parsed and followed
	List<File> scripts;

	private final Configuration conf = new Configuration();

	ClassSpec chObject = null;

	public void setStcripts(List<File> scripts) {
		// TODO check - make copy
		this.scripts = new ArrayList<File>(scripts);
	}

	@Override
	public Configuration getConfiguration(final Map<String, String> properties) {

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
			SOURCE_URL = (String) expr.evaluate(doc, XPathConstants.STRING);
			logger.info("source url: " + SOURCE_URL);

			// parse owlSetSchemas
			// <tr> <td>owlSetSchemas</td> <td></td>
			// <td>http://kub1x.org/dip/gen/</td> </tr>
			// expr = xpath
			// .compile("//tbody/tr/td[text()='owlSetSchemas']/../td[3]");
			// ID_PREFIX = (String) expr.evaluate(doc, XPathConstants.STRING);
			// conf.setBaseOntoPrefix(ID_PREFIX);
			// logger.info("id prefix: " + ID_PREFIX);

			// <tr> <td>owlSetSchemas</td> <td></td>
			// <td>http://kub1x.org/dip/gen/</td> </tr>
			expr = xpath.compile("//tbody/tr");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			logger.info("got <tr> list of length " + nl.getLength());

			// now parse one command after another and fire them ;)
			XPathExpression td1 = xpath.compile("./td[1]");
			XPathExpression td2 = xpath.compile("./td[2]");
			XPathExpression td3 = xpath.compile("./td[3]");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				
				String cmd = (String) td1.evaluate(n, XPathConstants.STRING);
				String tgt = (String) td2.evaluate(n, XPathConstants.STRING);
				String val = (String) td3.evaluate(n, XPathConstants.STRING);

				logger.info("parsed command: " + cmd + " target: " + tgt
						+ " value: " + val);

				fireCommand(cmd, tgt, val);
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return conf;
	}

	private void fireCommand(String command, String target, String value) {
		switch (command) {
		case "owlSetSchemas":
			// http://kub1x.org/dip/gen/
			String ID_PREFIX = value;
			conf.setBaseOntoPrefix(ID_PREFIX);
			conf.setSchemas(new String[] { ID_PREFIX });
			break;

		case "owlSetPublisher":
			conf.setPublisher(value);
			break;

		case "owlSetNextPageResolver":
			conf.setNextPageResolver(new EnumeratedNextPageResolver(value));
			break;

		case "owlSetEncoding":
			conf.setEncoding(value); // "iso-8859-2"
			break;

		case "owlSetLang":
			conf.setLang(value); // "cs"
			break;

		case "owlSetId":
			conf.setId(Utils.getFullId(value)); // ex. value: "person"
			break;

		case "owlCreateChObject":
			// Get base object (for each tr in table)
			// ex. value: "personRecord"
			chObject = Factory.createClassSpec(SeleniumModel
					.getOntClassForName(value).getURI());
			// ex. target: "table tr"
			conf.addInitialDefinition(Factory.createInitialDefinition(chObject,
					Factory.createJSoupSelector(css2jsoup(target))));
			break;

		case "owlAddSpec":
			// Get it's first name spec
			chObject.addSpec(true, Factory.createDPSpec(
					Factory.createJSoupSelector(css2jsoup(target)),
					SeleniumModel.getOntPropertyForName(value).getURI()));
			// chObject.addSpec(true, Factory.createDPSpec(
			// Factory.createJSoupSelector("td:eq(0)"),
			// SeleniumModel.hasFirstName.getURI()));
			// chObject.addSpec(true, Factory.createDPSpec(
			// Factory.createJSoupSelector("td:eq(1)"),
			// SeleniumModel.hasFamilyName.getURI()));
			// chObject.addSpec(Factory.createDPSpec(
			// Factory.createJSoupSelector("td:eq(2)"),
			// SeleniumModel.hasPhoneNumber.getURI()));
			break;

		default:
			break;
		}
	}

	private String css2jsoup(String target) {
		if (target.startsWith("css="))
			target = target.replaceFirst("css=", "");
		return target;
	}

}
