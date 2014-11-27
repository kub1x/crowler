/**
 * 
 */
package cz.sio2.crowler.ontology;

import java.util.Map;
import java.util.WeakHashMap;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;

/**
 * @author kub1x
 *
 */
public class ResourceCache {

    private Map<String, OntClass> classCache = new WeakHashMap<String, OntClass>();
    private Map<String, OntProperty> propertyCache = new WeakHashMap<String, OntProperty>();

    public OntClass getClass(String iri) {
        return this.classCache.get(iri);
    }

    public OntProperty getProperty(String iri) {
        return this.propertyCache.get(iri);
    }

    public void putClass(String iri, OntClass clazz) {
        this.classCache.put(iri, clazz);
    }

    public void putProperty(String iri, OntProperty property) {
        this.propertyCache.put(iri, property);
    }

}
