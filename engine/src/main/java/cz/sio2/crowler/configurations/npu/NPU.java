package cz.sio2.crowler.configurations.npu;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.DC;
import cz.sio2.crowler.JenaConnector;
import cz.sio2.crowler.Utils;
import cz.sio2.crowler.model.PropertyType;

public class NPU {
    public static final String MONDIS_IRI = "http://onto.mondis.cz/resource/";

    public static String idPrefix = MONDIS_IRI + "npu/";
    public static final String language = "cs";

    public static final OntModel schema;
    static {
        schema = ModelFactory.createOntologyModel();
    }

    public static OntClass monumnetRecord = Utils.c(idPrefix + "MonumnetRecord", language, schema, "Záznam v rejstříku Monumnet");
    public static OntProperty hasAnnotation = Utils.p(idPrefix + "hasAnnotation", language, schema, PropertyType.DATA, "má anotaci", "Anotace");
    public static OntProperty hasState = Utils.p(idPrefix + "hasState", language, schema, PropertyType.DATA, "má stav", "Stav");
    public static OntProperty hasEndangeredScope = Utils.p(idPrefix + "hasEndangeredScope", language, schema, PropertyType.DATA, "má záběr ohrožení", "Ohrožen");

    public static OntProperty hasNumber = Utils.p(idPrefix + "hasNumber", language, schema, PropertyType.DATA, "má číslo rejstříku", "Číslo rejstříku");
    // TODO uz

    public static OntClass district = Utils.c(idPrefix + "District", language, schema, "Okres");
    public static OntProperty hasDistrict = Utils.p(idPrefix + "hasDistrict", language, schema, PropertyType.OBJECT, "má okres", "Okres");

    public static OntProperty hasLocalityName = Utils.p(idPrefix + "hasLocalityName", language, schema, PropertyType.DATA, "má název sídelního útvaru", "Sídelní útvar");
    public static OntProperty hasIdentifiableLandRegistry = Utils.p(idPrefix + "hasIdentifiableLandRegistry", language, schema, PropertyType.DATA, "má část obce", "Část obce");

    static {
        hasIdentifiableLandRegistry.addProperty(DC.description, "Definice pojmu 'Část obce' z wikipedie: Evidenční sídelní jednotka se samostatnou řadou čísel popisných a evidenčních.");
    } 

    public static OntProperty hasLandRegistryNumber = Utils.p(idPrefix + "hasLandRegistryNumber", language, schema, PropertyType.DATA, "má číslo popisné", "čp.");
    public static OntProperty hasLocation = Utils.p(idPrefix + "hasLocation", language, schema, PropertyType.DATA, "má umístění", "Ulice,nám./umístění");
    public static OntProperty hasHouseNumber = Utils.p(idPrefix + "hasHouseNumber", language, schema, PropertyType.DATA, "má číslo orientační", "č.or.");

    public static OntProperty hasAddress = Utils.p(idPrefix + "hasAddress", language, schema, PropertyType.DATA, "má adresu",  "má adresu"); // custom

    public static OntClass historicCountry = Utils.c(idPrefix + "HistoricCountry", language, schema, "Historická země");
    public static OntProperty hasHistoricCountry = Utils.p(idPrefix + "hasHistoricCountry", language, schema, PropertyType.OBJECT, "má historickou zemi", "Historická země");
    public static OntProperty isRegisteredMonumentSince = Utils.p(idPrefix + "isRegisteredMonumentSince", language, schema, PropertyType.DATA, "je registrovanou památkou od", "Památkou od");
    public static OntProperty hasProtectionState = Utils.p(idPrefix + "hasProtectionState", language, schema, PropertyType.DATA, "má stav ochrany", "Ochrana stav/typ uzavření");

    public static OntClass buildingOffice = Utils.c(idPrefix + "BuildingOffice", language, schema, "Stavební úřad");
    public static OntProperty hasBuildingOffice = Utils.p(idPrefix + "hasBuildingOffice", language, schema, PropertyType.OBJECT, "má stavební úřad", "Stavební úřad");

    public static OntClass revenueOffice = Utils.c(idPrefix + "RevenueOffice", language, schema, "Finanční úřad");
    public static OntProperty hasRevenueOffice = Utils.p(idPrefix + "hasRevenueOffice", language, schema, PropertyType.OBJECT, "má finanční úřad", "Finanční úřad");

    public static OntClass landregistryrecord = Utils.c(idPrefix + "LandRegistryRecord", language, schema, "Záznam v katastru nemovitostí");
    public static OntProperty hasLandRegistryRecord = Utils.p(idPrefix + "hasLandRegistryRecord", language, schema, PropertyType.OBJECT, "má záznam v katastru nemovitostí", "Parcely");

    public static OntClass landregistry = Utils.c(idPrefix + "LandRegistry", language, schema, "Katastr nemovitostí");
    public static OntProperty hasLandRegistry = Utils.p(idPrefix + "hasLandRegistry", language, schema, PropertyType.OBJECT, "má katastrální území", "Katastrální území");

    public static OntProperty hasCadastralNumber = Utils.p(idPrefix + "hasCadastralNumber", language, schema, PropertyType.DATA, "má parcelní číslo", "parc.");
    public static OntProperty hasProtectionRestriction = Utils.p(idPrefix + "hasProtectionRestriction", language, schema, PropertyType.DATA, "má omezení památkové ochrany", "omezení památkové ochrany:");

    public static OntProperty hasCHObjectNumber = Utils.p(idPrefix + "hasCHObjectNumber", language, schema, PropertyType.DATA, "má číslo rejstříku objektů", " Č.rejst.");
    public static OntClass protectedAreaType = Utils.c(idPrefix + "ProtectedAreaType", language, schema, "Typ chráněného území");
    public static OntProperty hasProtectedAreaType = Utils.p(idPrefix + "hasProtectedAreaType", language, schema, PropertyType.OBJECT, "má typ chráněného území", "Typ chráněného území");

    public static OntProperty hasIdRegNumber = Utils.p(idPrefix + "hasIdRegNumber", language, schema, PropertyType.DATA, "má identifikátor záznamu", "Identifikátor záznamu (IdReg)");

//    OntClass district = Utils.c(idPrefix+"District", language, schema, "Okres");
//    OntProperty hasDistrict = Utils.p(idPrefix+"hasDistrict", language, schema, PropertyType.OBJECT, "má okres", "Okres");

    public static OntProperty isRegisteredInTimeSpecification = Utils.p(idPrefix+"isRegisteredInTimeSpecification", language, schema, PropertyType.OBJECT, "je registrován v době", "Od roku");
    public static OntProperty hasPart = Utils.p(idPrefix+"hasPart", language, schema, PropertyType.OBJECT, "má část", "Jiný objekt jako část objektu");

    public static void write(JenaConnector c) {
        c.connect();
        Model m = c.getModel(idPrefix+"vocab/monumnet");
        m.add(schema);
        c.closeModel(m);
        c.disconnect();
    }
}
