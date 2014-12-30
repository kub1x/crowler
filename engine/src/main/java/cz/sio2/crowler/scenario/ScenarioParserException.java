/**
 * 
 */
package cz.sio2.crowler.scenario;

import org.json.JSONException;

/**
 * Exception thrown by parser when an actual error in syntax occurs. This prevents us from throwing all JSONExceptions as some fields are optional, but would still cause the
 * JSONException to be thrown when missing.
 * 
 * @author kub1x
 */
public class ScenarioParserException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param msg
     * @param e
     */
    public ScenarioParserException(String msg, JSONException e) {
        super(msg, e);
    }

}
