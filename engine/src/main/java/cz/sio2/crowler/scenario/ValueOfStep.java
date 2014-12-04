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
    private String property;
    private Selector selector;

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

    // ---------------------------------------------------------------

}
