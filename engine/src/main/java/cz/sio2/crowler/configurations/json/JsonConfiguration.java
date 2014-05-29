package cz.sio2.crowler.configurations.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.openjena.atlas.json.JSON;
import org.openjena.atlas.json.JsonArray;
import org.openjena.atlas.json.JsonObject;
import org.openjena.atlas.json.JsonString;
import org.openjena.atlas.json.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cz.sio2.crowler.Factory;
import cz.sio2.crowler.Utils;
import cz.sio2.crowler.configurations.kub1x.KbxModel;
import cz.sio2.crowler.model.ClassSpec;
import cz.sio2.crowler.model.Configuration;
import cz.sio2.crowler.model.ConfigurationFactory;
import cz.sio2.crowler.model.EnumeratedNextPageResolver;
import cz.sio2.crowler.model.PropertyType;

public class JsonConfiguration implements ConfigurationFactory {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(JsonConfiguration.class
			.getName());

	final String SOURCE_URL = "http://www.inventati.org/kub1x/t/";
	// String SOURCE_URL = "";// "http://localhost:8888/";

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
		// Parse model
		// parseClasses((JsonArray) jsc.get(CLASSES));
		// parseProperties((JsonArray) jsc.get(PROPERTIES));
		// Parse steps
		parseSteps((JsonArray) jsc.get(STEPS), conf);
		// Parse ontology setting
		final String idPrefix = "http://kub1x.org/dip/"; //"http://xmlns.com/foaf/0.1/";
		conf.setNextPageResolver(new EnumeratedNextPageResolver(SOURCE_URL));
		// TODO from settings
		conf.setSchemas(new String[] { idPrefix });
		// TODO from settings
		conf.setEncoding("iso-8859-2");
		// TODO from settings
		conf.setLang("cs");
		// TODO from settings
		conf.setPublisher(SOURCE_URL);
		// TODO from settings
		conf.setId(Utils.getFullId("person"));
		// TODO from settings
		conf.setBaseOntoPrefix(idPrefix);
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

		logger.debug("parsed class spec with IRI: " + iri + ", selector: "
				+ selector);
		System.out.println("parsed class spec with IRI: " + iri
				+ ", selector: " + selector);

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

		logger.debug("parsed spec with iri: " + iri + ", and selector: "
				+ selector);
		System.out.println("parsed spec with iri: " + iri + ", and selector: "
				+ selector);

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

	private String getString(JsonObject o, String p) {
		JsonValue v = o.get(p);
		JsonString s = v.getAsString();
		return s.value();
	}

}
