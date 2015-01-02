package cz.sio2.crowler.selenium;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
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
import cz.sio2.crowler.scenario.NarrowStep;
import cz.sio2.crowler.scenario.OntoElemStep;
import cz.sio2.crowler.scenario.OntologyConfig;
import cz.sio2.crowler.scenario.Scenario;
import cz.sio2.crowler.scenario.Selector;
import cz.sio2.crowler.scenario.Step;
import cz.sio2.crowler.scenario.Template;
import cz.sio2.crowler.scenario.ValueOfStep;

public class WebDriverCrawler implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(WebDriverCrawler.class.getName());

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

        this.ontology.init(this.scenario.getName());

        // -----------------------------------

        OntologyConfig ontologyConfig = this.scenario.getOntologyConfig();

        if (ontologyConfig != null) {
            // TODO Potrebujeme seznam importu
            // e.x.: http://onto.mondis.cz/resource/npu/
            Map<String, String> imports = ontologyConfig.getImports();
            for (final String prefix : imports.keySet()) {
                String uri = imports.get(prefix);
                this.ontology.addImport(uri);
                this.ontology.setPrefix(prefix, uri);
                System.out.println("adding prefix: " + prefix + ":" + uri);
            }
        }

        // -----------------------------------

        // TODO consider NOT using call-template here. Use URL and INIT-TEMPLATE-NAME instead and just call callTemplate() directly.
        handleStep(this.scenario.getInitCallTemplate(), NO_PARENT, NO_CONTEXT, null);
    }

    // -------------------------------------------------------------------------

    private void callTemplate(String name, String url, Individual parent) {
        if (logger.isTraceEnabled()) {
            logger.trace("callTemplate(" + name + ", " + url + ") - start");
        }

        Template template = this.scenario.findTemplate(name);
        if (template == null) {
            throw new RuntimeException("Unable to find template: " + name);
        }

        // No URL, no job...
        if (url == null || url == "") {
            return;
        }

        // Self closeable call to web context
        try (WebContext web = new WebContext()) {
            web.goTo(url);
            handleSteps(template.getSteps(), parent, web.getBody(), web);
        } // web.close();
    }

    // -------------------------------------------------------------------------

    /**
     * 
     * @param steps
     * @param parent
     * @param context
     */
    private void handleSteps(List<Step> steps, Individual parent, WebElement context, WebContext web) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleSteps(" + steps + ", " + parent + ", " + context + ") - start");
        }

        if (steps == null) {
            return;
        }

        for (Step step : steps) {
            switch (step.getCommand()) {
            case ONTO_ELEM:
                handleStep((OntoElemStep) step, parent, context, web);
                break;
            case VALUE_OF:
                handleStep((ValueOfStep) step, parent, context, web);
                break;
            case CALL_TEMPLATE:
                handleStep((CallTemplateStep) step, parent, context, web);
                break;
            case NARROW:
                handleStep((NarrowStep) step, parent, context, web);
                break;
            // case USER_EVENT:
            // handleStep((UserEventStep) step, parent, context, web);
            // break;
            default:
                throw new RuntimeException("unknown command");
            }
        }
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    /**
     * 
     * @param step
     * @param parent
     * @param context
     */
    private void handleStep(CallTemplateStep step, Individual parent, WebElement context, WebContext web) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleStep(" + step + ", " + parent + ", " + context + ") - start");
        }

        String url = "";

        ValueOfStep valueOfStep = step.getValueOfStep();
        if (valueOfStep != null) {
            // Hiding parent as this valueOfStep should only return URL value
            url = handleStep(valueOfStep, NO_PARENT, context, web);
        }

        if (url == null || url == "") {
            Selector selector = step.getSelector();
            if (selector != null) {
                try {
                    url = selector.getText(context);
                } catch (NoSuchElementException e) {
                    // There is selector for next step, but nowhere to go...
                    return;
                }
            }
        }

        // TODO what are the actual conditions for invalid URL here? Test URL validity.
        if (url == null || url == "") {
            url = step.getUrl();
        }

        // TODO handle this properly: neither selector, nor manually added URL
        if (url == null || url == "") {
            // nowhere to go...
            return;
        }

        callTemplate(step.getTemplateName(), url, parent);
    }

    // -------------------------------------------------------------------------

    /**
     * Handle OntoElemStep within context specified.
     * 
     * @param step
     * @param context
     */
    private void handleStep(OntoElemStep step, Individual parent, WebElement context, WebContext web) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleStep(" + step + ", " + parent + ", " + context + ") - start");
        }

        String typeof = step.getTypeof();
        String rel = step.getRel();

        OntClass clazz = this.ontology.getOntClass(typeof);

        Selector selector = step.getSelector();
        List<WebElement> elements = selector.findElements(context);
        for (WebElement element : elements) {

            // TODO handle the id generation process
            String id = null;
            Individual individual = this.ontology.createEmptyIndividual(id, clazz);

            if (parent != null && rel != null && rel != "") {
                OntProperty ontProperty = this.ontology.getOntProperty(rel, PropertyType.OBJECT);
                parent.addProperty(ontProperty, individual);
            }

            handleSteps(step.getSteps(), individual, element, web);

        }
    }

    // -------------------------------------------------------------------------

    private String handleStep(ValueOfStep step, Individual parent, WebElement context, WebContext web) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleStep(" + step + ", " + parent + ", " + context + ") - start");
        }

        String property = step.getProperty();
        Selector selector = step.getSelector();
        List<WebElement> elements = selector.findElements(context);

        if (property != null && property != "") {
            if (parent == null) {
                throw new RuntimeException("No parent individual to assign property to. ");
            }
            for (WebElement element : elements) {
                OntProperty ontProperty = this.ontology.getOntProperty(property, PropertyType.DATA);
                String text = element.getText();
                // TODO Figure out the datatype (currently defaults to XSD_STRING) of this literal.
                // It shall fit the specification of the property in our included schemas.
                parent.addProperty(ontProperty, this.ontology.createLiteral(Vocabulary.XSD_STRING, text));
            }
        } else {
            String result = null;
            result = selector.getText(context);

            // TODO if (result != null)
            return result;

            // TODO the rest of value-of algorithm
            // if (step.hasRegex())
            // if (step.hasReplace())
            // if (step.hasValue())
            // result = step.getValue();
        }

        return null;
    }

    // -------------------------------------------------------------------------

    /**
     * NarrowStep only narrows web context.
     * 
     * @param step
     * @param parent
     * @param context
     */
    private void handleStep(NarrowStep step, Individual parent, WebElement context, WebContext web) {
        Selector selector = step.getSelector();
        List<WebElement> elements = selector.findElements(context);
        for (WebElement element : elements) {
            handleSteps(step.getSteps(), parent, element, web);
        }
    }

    // -------------------------------------------------------------------------

    // /**
    // *
    // * @param step
    // * @param parent
    // * @param context
    // */
    // private void handleStep(UserEventStep step, Individual parent, WebElement context, WebContext web) {
    // switch (step.getValue()) {
    // case (UserEventStep.BACK_EVT):
    // web.back();
    // break;
    //
    // case (UserEventStep.CLICK_EVT):
    // context.click();
    // break;
    //
    // default:
    // throw new RuntimeException("Unimplemented user-event: " + step.getValue());
    // }
    // }

    // -------------------------------------------------------------------------

    @Override
    public void close() throws Exception {
        this.ontology.close();
    }

}
