package cz.sio2.crowler.configurations.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.openjena.atlas.json.JSON;
import org.openjena.atlas.json.JsonArray;
import org.openjena.atlas.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cz.sio2.crowler.Factory;
import cz.sio2.crowler.Utils;
import cz.sio2.crowler.model.ClassSpec;
import cz.sio2.crowler.model.Configuration;
import cz.sio2.crowler.model.ConfigurationFactory;
import cz.sio2.crowler.model.PropertyType;

public class JsonConfiguration implements ConfigurationFactory {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(JsonConfiguration.class
			.getName());

	// final String SOURCE_URL = "http://www.inventati.org/kub1x/t/";
	String SOURCE_URL = "";// "http://localhost:8888/";

	File scenario;

	public void setScenario(File scenario) {
		this.scenario = scenario;
	}

	// ---------------------------------------------------------------------------------------------

	final String language = "en";
	static final OntModel model;
	static {
		model = ModelFactory.createOntologyModel();
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public Configuration getConfiguration(final Map<String, String> properties) {

		final Configuration conf = new Configuration();

		try {
			JsonObject jsc = JSON.parse(new FileInputStream(this.scenario));

			// Do the dirty job
			parseScenario(jsc, conf);

			// TODO add annotations generated by crowler "on the fly" containing
			// information about how the stuff was crawled (selectors, indexes
			// etc) into the model.

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return conf;
	}

	// ---------------------------------------------------------------------------------------------

	Map<String, OntClass> classes = new HashMap<>();
	Map<String, OntProperty> properties = new HashMap<>();

	// ---------------------------------------------------------------------------------------------
	
	final String CLASSES = "classes";
	final String PROPERTIES = "properties";
	final String STEPS = "steps";
	final String URI = "uri";
	final String NAME = "name";
	final String RESOURCE = "resource";
	final String SELECTOR = "selector";
	final String CHILDREN = "children";

	void parseScenario(JsonObject jsc, Configuration conf) {
		parseClasses((JsonArray) jsc.get(CLASSES));
		parseProperties((JsonArray) jsc.get(PROPERTIES));
		parseSteps((JsonArray) jsc.get(CLASSES), conf);
	}

	private void parseClasses(JsonArray aClasses) {
		for (int i = 0; i < aClasses.size(); i++) {
			JsonObject aClass = aClasses.get(i).getAsObject();
			parseClass(aClass);
		}
	}

	private void parseClass(JsonObject aClass) {

		String uri = getString(aClass, URI);
		String name = getString(aClass, NAME);

		OntClass value = Utils.c(uri, language, model, name);

		classes.put(uri, value);
	}

	private void parseProperties(JsonArray aProperties) {
		for (int i = 0; i < aProperties.size(); i++) {
			JsonObject aProperty = aProperties.get(i).getAsObject();
			parseProperty(aProperty);
		}
	}

	private void parseProperty(JsonObject aProperty) {
		String uri = getString(aProperty, URI);
		String name = getString(aProperty, NAME);

		// TODO need property type {DATA, OBJECT, ANNOTATION} !!!
		// TODO need an actual "readable" name of the property and
		// TODO need a name for property object (the last parameter)

		OntProperty value = Utils.p(uri, language, model, PropertyType.DATA,
				name, name); // XXX RRRRRRRrrrrrr!#@!#!@$#!

		properties.put(uri, value);
	}

	private void parseSteps(JsonArray aSteps, Configuration conf) {
		for (int i = 0; i < aSteps.size(); i++) {
			JsonObject aStep = aSteps.get(i).getAsObject();
			parseInitialClassSpec(aStep, conf);
		}
	}

	private void parseInitialClassSpec(JsonObject aStep, Configuration conf) {
		String iri = getString(aStep, RESOURCE);
		String selector = getString(aStep, SELECTOR);

		ClassSpec classSpec = Factory.createClassSpec(iri);

		conf.addInitialDefinition(Factory.createInitialDefinition(classSpec,
				Factory.createJSoupSelector(selector)));

		JsonArray children = (JsonArray) aStep.get(CHILDREN);

		for (int i = 0; i < children.size(); i++) {
			parseSpec(children.get(i).getAsObject(), classSpec, conf);
		}
	}

	private void parseSpec(JsonObject aStep, ClassSpec classSpec,
			Configuration conf) {
		boolean isPartOfId = true; // TODO we need this prop
		String iri = getString(aStep, RESOURCE);
		String selector = getString(aStep, SELECTOR);

		classSpec.addSpec(isPartOfId, Factory.createDPSpec(
				Factory.createJSoupSelector(selector), iri));

		/**
		 * createDPSpec() has two more attributes:
		 * 
		 * dataTypeIRI - Vocabulary.XSD_STRING, Vocabulary.foafName,
		 * allowEmptyValues -
		 */

		// TODO fire recursion for children
	}

	// ---------------------------------------------------------------------------------------------
	
	String getString(JsonObject o, String p) {
		return o.get(p).getAsString().value();
	}

}
