package cz.sio2.crowler.selenium.scenario;

import java.io.IOException;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Scenario {
	
	public static Scenario parse(String filePath) {
		if (filePath == null) {
			throw new RuntimeException("chybi scenar");
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc;

		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(filePath);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Scenario s = new Scenario();

		return s;
	}

	public List<String> getSchemas() {
		// TODO Auto-generated method stub
		return null;
	}

}
