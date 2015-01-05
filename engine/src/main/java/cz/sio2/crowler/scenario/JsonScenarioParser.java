/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kub1x
 *
 */
public class JsonScenarioParser {
    private static Logger logger = LoggerFactory.getLogger(JsonScenarioParser.class.getName());

    // scenario
    private static final String NAME_KEY = "name";
    private static final String ONTOLOGY_KEY = "ontology";
    private static final String CALL_TEMPLATE_KEY = "call-template";
    private static final String TEMPLATES_KEY = "templates";

    // ontology
    private static final String BASE_KEY = "base";
    private static final String IMPORTS_KEY = "imports";

    // import
    private static final String PREFIX_KEY = "prefix";
    private static final String URI_KEY = "uri";

    // call-template
    // NAME_KEY - from scenario
    private final static String URL_KEY = "url";

    // template
    // NAME_KEY - from scenario
    private final static String STEPS_KEY = "steps";

    // step
    private static final String VALUEOF_KEY = "valueof";
    private static final String COMMAND_KEY = "command";
    private static final String TYPEOF_KEY = "typeof";
    private static final String REL_KEY = "rel";
    private static final String SELECTOR_KEY = "selector";
    private static final String PROPERTY_KEY = "property";

    // selector
    private static final String VALUE_KEY = "value";
    private static final String TYPE_KEY = "type";
    private static final String CSS_SELECTOR_TYPE = "css";
    private static final String XPATH_SELECTOR_TYPE = "xpath";
    private static final String CHAINED_SELECTOR_TYPE = "chained";
    private static final String TEXT_KEY = "text";

    // private static final String REGEXP_KEY = "regexp";
    // private static final String REPLACE_KEY = "replace";

    /**
     * Private constructor.
     */
    private JsonScenarioParser() {
    }

    /**
     * Parsing factory method.
     * 
     * @param filePath
     * @return
     * @throws FileNotFoundException
     * @throws JSONException
     */
    public static Scenario parse(String filePath) throws FileNotFoundException, JSONException {
        if (logger.isTraceEnabled()) {
            logger.trace("parse(" + filePath + ") - start");
        }
        return parse(new FileReader(filePath));
    }

    /**
     * Parsing factory method.
     * 
     * @param reader
     * @return
     * @throws JSONException
     */
    public static Scenario parse(Reader reader) throws JSONException {
        if (logger.isTraceEnabled()) {
            logger.trace("parse(" + reader + ") - start");
        }
        JSONObject json = new JSONObject(new JSONTokener(reader));
        Scenario result = new Scenario();
        populate(result, json);
        return result;
    }

    // -------------------------------------------------------------------------

    private static void populate(Scenario scenario, JSONObject jsonScenario) throws JSONException {
        if (logger.isTraceEnabled()) {
            logger.trace("populate(" + scenario + ", " + jsonScenario + ") - start");
        }
        scenario.setName(getStringPropertyOrEmpty(jsonScenario, NAME_KEY));
        scenario.setInitCallTemplate((CallTemplateStep) parseStep(jsonScenario.getJSONObject(CALL_TEMPLATE_KEY)));
        scenario.setOntologyConfig(parseOntologyConfig(jsonScenario));
        populateTemplates(scenario, jsonScenario);
    }

    // -------------------------------------------------------------------------

    private static OntologyConfig parseOntologyConfig(JSONObject jsonScenario) {
        if (logger.isTraceEnabled()) {
            logger.trace("parseOntologyConfig(" + jsonScenario + ") - start");
        }
        JSONObject jsonOntologyConfig = jsonScenario.getJSONObject(ONTOLOGY_KEY);
        OntologyConfig ontologyConfig = new OntologyConfig();
        ontologyConfig.setBase(getStringPropertyOrEmpty(jsonOntologyConfig, BASE_KEY));
        populateImports(ontologyConfig, jsonOntologyConfig);
        return ontologyConfig;
    }

    private static void populateImports(OntologyConfig ontologyConfig, JSONObject jsonOntologyConfig) {
        if (logger.isTraceEnabled()) {
            logger.trace("populateImports(" + ontologyConfig + ", " + jsonOntologyConfig + ") - start");
        }
        try {
            JSONArray imports = jsonOntologyConfig.getJSONArray(IMPORTS_KEY);
            for (int i = 0; i < imports.length(); i++) {
                JSONObject jsonImport = imports.getJSONObject(i);
                String prefix = getStringPropertyOrEmpty(jsonImport, PREFIX_KEY);
                String uri = getStringPropertyOrEmpty(jsonImport, URI_KEY);
                if (!prefix.isEmpty() && !uri.isEmpty()) {
                    ontologyConfig.putImport(prefix, uri);
                }
            }
        } catch (JSONException e) {
            // No imports, no cry
            return;
        }
    }

    // -------------------------------------------------------------------------

    private static void populateTemplates(Scenario scenario, JSONObject jsonScenario) {
        if (logger.isTraceEnabled()) {
            logger.trace("populateTemplates(" + scenario + ", " + jsonScenario + ") - start");
        }
        JSONArray templates = jsonScenario.getJSONArray(TEMPLATES_KEY);
        for (int i = 0; i < templates.length(); i++) {
            JSONObject jsonTemplate = templates.getJSONObject(i);
            Template template = new Template();
            template.setName(getStringPropertyOrEmpty(jsonTemplate, NAME_KEY));
            populateSubSteps(template, jsonTemplate);
            scenario.addTemplate(template);
        }

    }

    // -------------------------------------------------------------------------

    private static void populateSubSteps(WithSubsteps result, JSONObject json) {
        if (logger.isTraceEnabled()) {
            logger.trace("populateSubSteps(" + result + ", " + json + ") - start");
        }
        try {
            JSONArray steps = json.getJSONArray(STEPS_KEY);
            for (int i = 0; i < steps.length(); i++) {
                JSONObject jsonStep = (JSONObject) steps.get(i);
                Step step = parseStep(jsonStep);
                result.addStep(step);
            }
        } catch (JSONException e) {
            // No sub-steps
            return;
        }

    }

    private static Step parseStep(JSONObject jsonStep) throws JSONException {
        if (logger.isTraceEnabled()) {
            logger.trace("populateStep(" + jsonStep + ") - start");
        }
        Command command = Command.byName(jsonStep.getString(COMMAND_KEY));
        Step result;
        switch (command) {
        case ONTO_ELEM:
            result = populate(new OntoElemStep(), jsonStep);
            break;

        case VALUE_OF:
            result = populate(new ValueOfStep(), jsonStep);
            break;

        case CALL_TEMPLATE:
            result = populate(new CallTemplateStep(), jsonStep);
            break;

        case NARROW:
            result = populate(new NarrowStep(), jsonStep);
            break;

        case USER_EVENT:
            result = populate(new UserEventStep(), jsonStep);
            break;

        default:
            throw new RuntimeException("Unnown or unimplemented command: " + command);
        }

        return result;
    }

    // -------------------------------------------------------------------------

    private static Step populate(OntoElemStep ontoElemStep, JSONObject jsonStep) {
        if (logger.isTraceEnabled()) {
            logger.trace("populate(" + ontoElemStep + ", " + jsonStep + ") - start");
        }
        ontoElemStep.setTypeof(getStringPropertyOrEmpty(jsonStep, TYPEOF_KEY));
        ontoElemStep.setRel(getStringPropertyOrEmpty(jsonStep, REL_KEY));
        ontoElemStep.setSelector(getSelector(jsonStep, SELECTOR_KEY));
        populateSubSteps(ontoElemStep, jsonStep);
        return ontoElemStep;
    }

    private static Step populate(ValueOfStep valueOfStep, JSONObject jsonStep) {
        if (logger.isTraceEnabled()) {
            logger.trace("populate(" + valueOfStep + ", " + jsonStep + ") - start");
        }
        valueOfStep.setProperty(getStringPropertyOrEmpty(jsonStep, PROPERTY_KEY));
        valueOfStep.setSelector(getSelector(jsonStep, SELECTOR_KEY));
        valueOfStep.setText(getStringPropertyOrNull(jsonStep, TEXT_KEY));
        // valueOfStep.setValue(getStringPropertyOrNull(jsonStep, VALUE_KEY));
        // valueOfStep.setRegexp(getStringPropertyOrNull(jsonStep, REGEXP_KEY));
        // valueOfStep.setReplace(getStringPropertyOrNull(jsonStep, REPLACE_KEY));
        return valueOfStep;
    }

    private static Step populate(CallTemplateStep callTemplateStep, JSONObject jsonStep) {
        if (logger.isTraceEnabled()) {
            logger.trace("populate(" + callTemplateStep + ", " + jsonStep + ") - start");
        }
        callTemplateStep.setTemplateName(getStringPropertyOrEmpty(jsonStep, NAME_KEY));
        callTemplateStep.setUrl(getStringPropertyOrEmpty(jsonStep, URL_KEY));
        callTemplateStep.setSelector(getSelector(jsonStep, SELECTOR_KEY));

        // Parse inner value-of
        try {
            ValueOfStep valueOfStep = new ValueOfStep();
            populate(valueOfStep, jsonStep.getJSONObject(VALUE_KEY));
            callTemplateStep.setValueOfStep(valueOfStep);
        } catch (JSONException e) {
            // Not mandatory - Do nothing...
        }

        return callTemplateStep;
    }

    private static Step populate(NarrowStep narrowStep, JSONObject jsonStep) {
        narrowStep.setSelector(getSelector(jsonStep, SELECTOR_KEY));
        return narrowStep;
    }

    private static Step populate(UserEventStep userEventStep, JSONObject jsonStep) {
        userEventStep.setValue(getStringPropertyOrEmpty(jsonStep, VALUE_KEY));
        return userEventStep;
    }

    // -------------------------------------------------------------------------

    private static Selector getSelector(JSONObject jsonStep, String key) {
        if (logger.isTraceEnabled()) {
            logger.trace("getSelector(" + jsonStep + ", " + key + ") - start");
        }
        try {
            return getSelector(jsonStep.getJSONObject(key));
        } catch (JSONException e) {
            // No selector
            return null;
        }
    }

    private static Selector getSelector(JSONObject jsonSelector) {
        if (logger.isTraceEnabled()) {
            logger.trace("getSelector(" + jsonSelector + ") - start");
        }
        try {
            String type = getStringPropertyOrEmpty(jsonSelector, TYPE_KEY);
            switch (type) {
            case CSS_SELECTOR_TYPE:
                return new CssSelector(getStringPropertyOrEmpty(jsonSelector, VALUE_KEY));

            case XPATH_SELECTOR_TYPE:
                return new XPathSelector(getStringPropertyOrEmpty(jsonSelector, VALUE_KEY));

            case CHAINED_SELECTOR_TYPE:
                ChainedSelector selector = new ChainedSelector();
                JSONArray jsonSelectors = jsonSelector.getJSONArray(VALUE_KEY);
                for (int i = 0; i < jsonSelectors.length(); i++) {
                    selector.addSelector(getSelector(jsonSelectors.getJSONObject(i)));
                }
                return selector;

            default:
                throw new NotImplementedException("Unknown selector type: " + type);

            }
        } catch (JSONException e) {
            throw new ScenarioParserException("Wrong selector object: " + jsonSelector, e);
        }

    }

    // -------------------------------------------------------------------------

    /**
     * Safely obtain string property from JSONObject.
     * 
     * @param obj
     * @param key
     * @return
     */
    private static String getStringPropertyOrEmpty(JSONObject obj, String key) {
        if (logger.isTraceEnabled()) {
            logger.trace("getStringPropertyOrEmpty(" + obj + ", " + key + ") - start");
        }
        try {
            if (obj != null && obj.has(key)) {
                return obj.getString(key);
            } else {
                return "";
            }

        } catch (JSONException e) {
            return "";
        }
    }

    private static String getStringPropertyOrNull(JSONObject obj, String key) {
        if (logger.isTraceEnabled()) {
            logger.trace("getStringPropertyOrNull(" + obj + ", " + key + ") - start");
        }
        try {
            if (obj != null && obj.has(key)) {
                return obj.getString(key);
            } else {
                return null;
            }

        } catch (JSONException e) {
            return null;
        }

    }
}
