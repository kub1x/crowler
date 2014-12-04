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
public class OntoElemStep extends Step implements WithSubsteps {

    // TODO change type to URI
    private String typeof;
    private Selector selector;
    private String rel;
    private List<Step> substeps;

    // -------------------------------------------------------------------------

    public OntoElemStep() {
        this.setCommand(Command.ONTO_ELEM);
        this.substeps = new ArrayList<>();
    }

    // -------------------------------------------------------------------------

    public String getTypeof() {
        return typeof;
    }

    public void setTypeof(String typeof) {
        this.typeof = typeof;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    @Override
    public List<Step> getSteps() {
        return this.substeps;
    }

    @Override
    public void setSteps(List<Step> steps) {
        this.substeps = steps;
    }

    @Override
    public void addStep(Step step) {
        this.substeps.add(step);
    }

}
