/**
 * 
 */
package cz.sio2.crowler.scenario;

/**
 * @author kub1x
 *
 */
public class ValueOfStep extends Step {

    // TODO change type to URI
    private String property = null;
    private Selector selector = null;
    private String text = null;
    private String value = null;
    private String regexp = null;
    private String replace = null;

    // ---------------------------------------------------------------

    public ValueOfStep() {
        this.setCommand(Command.VALUE_OF);
    }

    // ---------------------------------------------------------------

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

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

    /**
     * @return the regexp
     */
    public String getRegexp() {
        return regexp;
    }

    /**
     * @param regexp
     *            the regexp to set
     */
    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    /**
     * @return the replace
     */
    public String getReplace() {
        return replace;
    }

    /**
     * @param replace
     *            the replace to set
     */
    public void setReplace(String replace) {
        this.replace = replace;
    }

    // ---------------------------------------------------------------

}
