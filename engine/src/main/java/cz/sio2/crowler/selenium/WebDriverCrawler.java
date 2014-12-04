package cz.sio2.crowler.selenium;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;

import cz.sio2.crowler.JenaConnector;
import cz.sio2.crowler.Vocabulary;
import cz.sio2.crowler.model.PropertyType;
import cz.sio2.crowler.ontology.OntologyContext;
import cz.sio2.crowler.scenario.CallTemplateStep;
import cz.sio2.crowler.scenario.OntoElemStep;
import cz.sio2.crowler.scenario.OntologyConfig;
import cz.sio2.crowler.scenario.Scenario;
import cz.sio2.crowler.scenario.Selector;
import cz.sio2.crowler.scenario.Step;
import cz.sio2.crowler.scenario.Template;
import cz.sio2.crowler.scenario.ValueOfStep;

public class WebDriverCrawler {
    private Logger logger = LoggerFactory.getLogger(WebDriverCrawler.class.getName());

    private Scenario scenario;

    private OntologyContext ontology;

    public WebDriverCrawler(JenaConnector connector, Scenario scenario) {
        this.scenario = scenario;
        this.ontology = new OntologyContext(connector);
    }

    private final static Individual NO_PARENT = null;
    private static final WebElement NO_CONTEXT = null;

    public void doIt() {
        if (logger.isTraceEnabled()) {
            logger.trace("doIt() - start");
        }

        this.ontology.init(scenario.getName());

        // -----------------------------------

        OntologyConfig ontologyConfig = scenario.getOntologyConfig();

        if (ontologyConfig != null) {
            // TODO Potrebujeme seznam importu
            // e.x.: http://onto.mondis.cz/resource/npu/
            Map<String, String> imports = ontologyConfig.getImports();
            for (final String prefix : imports.keySet()) {
                String uri = imports.get(prefix);
                ontology.addImport(uri);
                ontology.setPrefix(prefix, uri);
                System.out.println("adding prefix: " + prefix + ":" + uri);
            }
        }

        // -----------------------------------

        handleStep(scenario.getInitCallTemplate(), NO_PARENT, NO_CONTEXT);

        // -----------------------------------

        this.ontology.close();

    }

    private void callTemplate(String name, String url) {
        if (logger.isTraceEnabled()) {
            logger.trace("callTemplate(" + name + ", " + url + ") - start");
        }

        Template template = scenario.findTemplate(name);
        if (template == null) {
            throw new RuntimeException("Unable to find template: " + name);
        }

        WebContext web = new WebContext();
        web.goTo(url);
        handleSteps(template.getSteps(), NO_PARENT, web.getBody());
        web.close();
    }

    // -------------------------------------------------------------------------

    /**
     * 
     * @param steps
     * @param parent
     * @param context
     */
    private void handleSteps(List<Step> steps, Individual parent, WebElement context) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleSteps(" + steps + ", " + parent + ", " + context + ") - start");
        }

        if (steps == null) {
            return;
        }

        for (Step step : steps) {
            switch (step.getCommand()) {
            case ONTO_ELEM:
                handleStep((OntoElemStep) step, parent, context);
                break;
            case VALUE_OF:
                handleStep((ValueOfStep) step, parent, context);
                break;
            case CALL_TEMPLATE:
                handleStep((CallTemplateStep) step, parent, context);
                break;
            default:
                throw new RuntimeException("unknown command");
            }
        }
    }

    /**
     * 
     * @param step
     * @param parent
     * @param context
     */
    private void handleStep(CallTemplateStep step, Individual parent, WebElement context) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleStep(" + step + ", " + parent + ", " + context + ") - start");
        }
        Selector selector = step.getSelector();
        String url = "";

        if (selector != null) {
            // WebElement elem = context.findElement(selector.getBy());
            url = selector.getText(context);
        }

        // TODO what are the actual conditions for invalid URL here? Test URL validity.
        if (url == null || url == "") {
            url = step.getUrl();
        }

        callTemplate(step.getTemplateName(), url);
    }

    /**
     * Handle OntoElemStep within context specified.
     * 
     * @param step
     * @param context
     */
    private void handleStep(OntoElemStep step, Individual parent, WebElement context) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleStep(" + step + ", " + parent + ", " + context + ") - start");
        }

        String typeof = step.getTypeof();
        String rel = step.getRel();

        OntClass clazz = this.ontology.getOntClass(typeof);

        Selector selector = step.getSelector();
        List<WebElement> elements = selector.findElements(context);// context.findElements(By.cssSelector(selector));
        for (WebElement element : elements) {

            // TODO handle the id generation process
            String id = null;
            Individual individual = this.ontology.createEmptyIndividual(id, clazz);

            if (parent != null && rel != null && rel != "") {
                OntProperty ontProperty = this.ontology.getOntProperty(rel, PropertyType.OBJECT);
                parent.addProperty(ontProperty, individual);
            }

            handleSteps(step.getSteps(), individual, element);

        }
    }

    // -------------------------------------------------------------------------

    private void handleStep(ValueOfStep step, Individual parent, WebElement context) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleStep(" + step + ", " + parent + ", " + context + ") - start");
        }

        String property = step.getProperty();
        Selector selector = step.getSelector();
        List<WebElement> elements = selector.findElements(context);
        for (WebElement element : elements) {
            if (parent != null && property != null && property != "") {
                OntProperty ontProperty = this.ontology.getOntProperty(property, PropertyType.DATA);
                String text = element.getText();
                // TODO Figure out the datatype (currently defaults to XSD_STRING) of this literal.
                // It shall fit the specification of the property in our included schemas.
                parent.addProperty(ontProperty, this.ontology.createLiteral(Vocabulary.XSD_STRING, text));
            }
        }
    }

}
