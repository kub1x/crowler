/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kub1x
 *
 */
public class OntologyConfig {

    private String base;
    private Map<String, String> imports;
    private Charset encoding = StandardCharsets.UTF_8;

    public OntologyConfig() {
        this.imports = new HashMap<>();
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, String> getImports() {
        return imports;
    }

    public void setImports(Map<String, String> imports) {
        this.imports = imports;
    }

    public void putImport(String prefix, String uri) {
        if (this.imports == null) {
            this.imports = new HashMap<>();
        }
        if (this.imports.containsKey(prefix)) {
            throw new RuntimeException("Overriding import on prefix: " + prefix + " [" + this.imports.get(prefix) + " -> " + uri + "]");
        }
        this.imports.put(prefix, uri);
    }

    public Charset getEncoding() {
        return encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }
}
