package cz.sio2.crowler.configurations.parser;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cz.sio2.crowler.Utils;
import cz.sio2.crowler.model.PropertyType;

public class SeleniumModel {
	public static final String IRI = "http://kub1x.org/";

	public static String idPrefix = IRI + "dip/";
	public static final String language = "cs";

	public static final OntModel schema;
	static {
		schema = ModelFactory.createOntologyModel();
	}

	public static OntClass personRecord = Utils.c(idPrefix + "PersonRecord",
			language, schema, "Záznam o osobě");
	public static OntProperty hasFirstName = Utils.p(idPrefix + "hasFirstName",
			language, schema, PropertyType.DATA, "má jméno", "Jméno");
	public static OntProperty hasFamilyName = Utils.p(idPrefix
			+ "hasFamilyName", language, schema, PropertyType.DATA,
			"má příjmení", "Příjmení");
	public static OntProperty hasPhoneNumber = Utils.p(idPrefix
			+ "hasPhoneNumber", language, schema, PropertyType.DATA,
			"má telefonní číslo", "Telefon");

	// TODO create map from "name" to OntClass/OntProperty

	public static OntClass getOntClassForName(String name) {
		switch (name) {
		case "personRecord":
			return personRecord;
		default:
			return null;
		}
	}

	public static OntProperty getOntPropertyForName(String name) {
		switch (name) {
		case "hasFirstName":
			return hasFirstName;
		case "hasFamilyName":
			return hasFamilyName;
		case "hasPhoneNumber":
			return hasPhoneNumber;
		default:
			return null;
		}
	}
}
