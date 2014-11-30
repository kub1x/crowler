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
public class Template implements WithSubsteps {

    private String name;
    private List<Step> steps;

    /**
     * Constructor.
     */
    public Template() {
        this.steps = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * TODO inherit javadoc
     * 
     * @see cz.sio2.crowler.scenario.WithSubsteps#getSteps()
     */
    @Override
    public List<Step> getSteps() {
        return steps;
    }

    /*
     * (non-Javadoc)
     * 
     * TODO inherit javadoc
     * 
     * @see cz.sio2.crowler.scenario.WithSubsteps#setSteps(java.util.List)
     */
    @Override
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    /*
     * (non-Javadoc)
     * 
     * TODO inherit javadoc
     * 
     * @see cz.sio2.crowler.scenario.WithSubsteps#addStep(cz.sio2.crowler.scenario .Step)
     */
    @Override
    public void addStep(Step step) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        this.steps.add(step);
    }

}
