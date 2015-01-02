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
public class NarrowStep extends Step implements WithSubsteps {

    private Selector selector;

    private List<Step> steps = new ArrayList<>();

    @Override
    public List<Step> getSteps() {
        return steps;
    }

    @Override
    public void setSteps(List<Step> steps) {
        // TODO really?
        this.steps = steps;
    }

    @Override
    public void addStep(Step step) {
        this.steps.add(step);
    }

    /**
     * @return the selector
     */
    public Selector getSelector() {
        return selector;
    }

    /**
     * @param selector
     *            the selector to set
     */
    public void setSelector(Selector selector) {
        this.selector = selector;
    }

}
