/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * @author kub1x
 *
 */
public class JsonScenarioParser {

    //
    final static Charset ENCODING = StandardCharsets.UTF_8;

    // scenario
    private final static String URL_KEY = "url";
    private final static String STEPS_KEY = "steps";

    // step
    private static final String COMMAND_KEY = "command";
    private static final String TYPEOF_KEY = "typeof";
    private static final String REL_KEY = "rel";
    private static final String SELECTOR_KEY = "selector";
    private static final String PROPERTY_KEY = "property";

    // selector
    private static final String VALUE_KEY = "value";
    private static final String TYPE_KEY = "key";

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
        JSONObject json = new JSONObject(new JSONTokener(reader));
        Scenario result = new Scenario();
        populate(result, json);
        return result;
    }

    // -------------------------------------------------------------------------

    private static void populate(Scenario result, JSONObject json) throws JSONException {
        result.setUrl(getStringPropertyOrEmpty(json, URL_KEY));
        populateSubSteps(result, json);
    }

    private static void populateSubSteps(WithSubsteps result, JSONObject json) {
        JSONArray steps = json.getJSONArray(STEPS_KEY);

        if (steps == null) {
            return;
        }

        for (int i = 0; i < steps.length(); i++) {
            JSONObject jsonStep = (JSONObject) steps.get(i);
            Step step = parseStep(jsonStep);
            result.addStep(step);
        }
    }

    private static Step parseStep(JSONObject jsonStep) throws JSONException {
        Command command = Command.byName(jsonStep.getString(COMMAND_KEY));
        Step result;
        switch (command) {
        case ONTO_ELEM:
            result = populate(new OntoElemStep(), jsonStep);
            break;

        case VALUE_OF:
            result = populate(new ValueOfStep(), jsonStep);
            break;

        default:
            throw new RuntimeException("Unnown or unimplemented command: " + command);
        }

        return result;
    }

    // -------------------------------------------------------------------------

    private static Step populate(ValueOfStep valueOfStep, JSONObject jsonStep) {
        valueOfStep.setProperty(getStringPropertyOrEmpty(jsonStep, PROPERTY_KEY));
        valueOfStep.setSelector(getSelector(jsonStep));
        return valueOfStep;
    }

    private static Step populate(OntoElemStep ontoElemStep, JSONObject jsonStep) {
        ontoElemStep.setTypeof(getStringPropertyOrEmpty(jsonStep, TYPEOF_KEY));
        ontoElemStep.setRel(getStringPropertyOrEmpty(jsonStep, REL_KEY));
        ontoElemStep.setSelector(getSelector(jsonStep));
        populateSubSteps(ontoElemStep, jsonStep);
        return ontoElemStep;
    }

    private static String getSelector(JSONObject jsonStep) {
        JSONObject jsonSelector = jsonStep.getJSONObject(SELECTOR_KEY);
        // TODO we ignore the "type" here as it's always JSOUP now
        return getStringPropertyOrEmpty(jsonSelector, VALUE_KEY);
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
}
