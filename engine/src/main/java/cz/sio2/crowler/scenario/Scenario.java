/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.util.HashMap;
import java.util.Map;

/**
 * Inner representation for Scenario object.
 * 
 * <code>
 * {
 *   name: "<String: scenario.name>", 
 *   ontology: {<OntologyConfig: scenario.ontologyConfiguration>}, 
 *   creation-date: "<Date: scenario.creationDate>", 
 *   call-template: {<CallTemplateStep: scenario.initCallTemplate>}, 
 *   templates: [<ScenarioTemplate: scenario.templates>], 
 * }
 * 
 * @author kub1x
 * 
 */
public class Scenario {

    private String name;
    private OntologyConfig ontologyConfig;
    private CallTemplateStep initCallTemplate;
    private Map<String, Template> templates;

    // -------------------------------------------------------------------------

    /**
     * Constructor.
     * 
     * @param name
     */
    public Scenario(String name) {
        this.name = name;
        this.templates = new HashMap<>();
    }

    /**
     * Constructor with empty name.
     */
    public Scenario() {
        this("");
    }

    // -------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OntologyConfig getOntologyConfig() {
        return ontologyConfig;
    }

    public void setOntologyConfig(OntologyConfig ontologyConfig) {
        this.ontologyConfig = ontologyConfig;
    }

    public CallTemplateStep getInitCallTemplate() {
        return initCallTemplate;
    }

    public void setInitCallTemplate(CallTemplateStep initCallTemplate) {
        this.initCallTemplate = initCallTemplate;
    }

    public Map<String, Template> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, Template> templates) {
        this.templates = templates;
    }

    public void addTemplate(Template template) {
        if (template == null) {
            return;
        }
        if (this.templates == null) {
            this.templates = new HashMap<>();
        }
        if (this.templates.containsKey(template.getName())) {
            throw new RuntimeException("Duplicit template name: " + template.getName());
        }
        this.templates.put(template.getName(), template);
    }

    public Template findTemplate(String name) {
        if (this.templates == null) {
            return null;
        }
        return this.templates.get(name);
    }

    // -------------------------------------------------------------------------

}
