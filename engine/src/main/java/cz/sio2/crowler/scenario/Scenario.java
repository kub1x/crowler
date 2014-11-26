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
public class Scenario {

    /**
     * Constructor.
     * 
     * @param name
     */
    public Scenario(String name) {
        this.name = name;
        this.steps = new ArrayList<>();
    }

    /**
     * Constructor with empty name.
     */
    public Scenario() {
        this("");
    }

    // -------------------------------------------------------------------------

    private String url;
    private String name;
    private List<Step> steps;

    // -------------------------------------------------------------------------

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    // -------------------------------------------------------------------------

    public void addStep(Step step) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        this.steps.add(step);
    }

}
