/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.util.List;

/**
 * @author kub1x
 *
 */
public interface WithSubsteps {

    public List<Step> getSteps();

    public void setSteps(List<Step> steps);

    public void addStep(Step step);

}
