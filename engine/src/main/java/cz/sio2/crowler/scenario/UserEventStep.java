/**
 * 
 */
package cz.sio2.crowler.scenario;

/**
 * @author kub1x
 *
 */
public class UserEventStep extends Step {

    public static final String CLICK_EVT = "click";
    public static final String BACK_EVT = "back";

    private String value;

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
