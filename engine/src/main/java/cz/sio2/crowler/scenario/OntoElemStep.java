/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kub1x
 *
 */
public class OntoElemStep extends Step {

    // TODO change type to URI
    private String typeof;
    private String selector;
    private String rel;
    private List<Step> children;

    // -------------------------------------------------------------------------

    public OntoElemStep() {
        this.setCommand(Command.ONTO_ELEM);
    }

    // -------------------------------------------------------------------------

    public String getTypeof() {
        return typeof;
    }

    public void setTypeof(String typeof) {
        this.typeof = typeof;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public List<Step> getChildren() {
        return children;
    }

    public void setChildren(List<Step> children) {
        this.children = children;
    }

    // -------------------------------------------------------------------------

    public void addChildren(Step step) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(step);
    }

}
