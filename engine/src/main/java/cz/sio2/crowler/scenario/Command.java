/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kub1x
 *
 */
public enum Command {

    ONTO_ELEM("onto-elem"),
    VALUE_OF("value-of"),
    CALL_TEMPLATE("call-template"),
    NARROW("narrow"),
    USER_EVENT("user-event");

    private final static Map<String, Command> BY_NAME;
    static {
        Map<String, Command> tmp = new HashMap<>();
        for (Command c : Command.values()) {
            tmp.put(c.name, c);
        }
        BY_NAME = Collections.unmodifiableMap(tmp);
    }

    public static Command byName(String name) {
        return BY_NAME.get(name);
    }

    private String name;

    private Command(String name) {
        this.name = name;
    }

}
