/**
 * 
 */
package cz.sio2.crowler.ontology;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cz.sio2.crowler.JenaConnector;
import cz.sio2.crowler.Utils;

/**
 * @author kub1x
 *
 */
public class OntologyContext implements AutoCloseable {

    private final JenaConnector connector;
    private final OntModel model;
    private final ResourceCache cache;

    private String baseOntoPrefix = "http://kub1x.org/onto/dip/t/";
    private Ontology ontology;
    private String ontologyId;

    public OntologyContext(JenaConnector connector) {
        this.connector = connector;
        this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        this.cache = new ResourceCache();
    }

    public void setPrefix(String prefix, String uri) {
        this.model.setNsPrefix(prefix, uri);
    }

    public Individual createEmptyIndividual(String id, OntClass clazz) {
        if (id == null) {
            id = "indiv-" + getCurrentTimeStamp();
        }
        return this.model.createIndividual(Utils.getUniqueIRI(model, baseOntoPrefix, id), clazz);
    }

    public Literal createLiteral(String rdfDataTypeIRI, String text) {
        if (rdfDataTypeIRI == null) {
            return model.createLiteral(text, "en");
        } else {
            if (rdfDataTypeIRI.startsWith("http://www.w3.org/2001/XMLSchema")) {
                return model.createTypedLiteral(text, new XSDDatatype(rdfDataTypeIRI.substring("http://www.w3.org/2001/XMLSchema#".length())));
            } else {
                return model.createTypedLiteral(text, new BaseDatatype(rdfDataTypeIRI));
            }
        }
    }

    public OntClass getOntClass(final String iri) {
        OntClass c = this.cache.getClass(iri);
        if (c == null) {
            c = model.createClass(iri);
            this.cache.putClass(iri, c);
        }
        return c;
    }

    public OntProperty getOntProperty(final String iri, final PropertyType type) {
        OntProperty c = this.cache.getProperty(iri);
        if (c == null) {
            switch (type) {
            case ANNOTATION:
                c = this.model.createAnnotationProperty(iri);
                break;
            case DATA:
                c = this.model.createDatatypeProperty(iri);
                break;
            case OBJECT:
                c = this.model.createObjectProperty(iri);
                break;
            }

            this.cache.putProperty(iri, c);
        }
        return c;
    }

    public void addImport(String importIri) {
        this.ontology.addImport(ModelFactory.createOntologyModel().createOntology(importIri));

    }

    public void init(final String scenarioName) {

        this.connector.connect();

        this.ontologyId = String.format("http://kub1x.org/onto/dip/t/%s-%s", scenarioName, getCurrentTimeStamp());
        this.baseOntoPrefix = ontologyId + "/";
        this.ontology = model.createOntology(ontologyId);

    }

    public void close() {
        // Persist
        final Model persistModel = this.connector.getModel(this.ontologyId);
        persistModel.add(model);
        // copy the prefixes from OntModel into Jena Model
        persistModel.setNsPrefixes(model.getNsPrefixMap());
        this.connector.closeModel(persistModel);

        this.connector.disconnect();
    }

    private static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHHmmssSSS").format(new Date());
    }

}
