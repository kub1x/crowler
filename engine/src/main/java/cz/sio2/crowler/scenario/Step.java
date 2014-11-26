/**
 * 
 */
package cz.sio2.crowler.scenario;

/**
 * @author kub1x
 *
 */
public class Step {

    /**
     * Determine class of the step by it's command property.
     * 
     * @param step
     * @return
     */
    public static Class<? extends Step> getStepClass(Step step) {
        return getStepClass(step.getCommand());
    }

    /**
     * Give Step class related to the command name.
     * 
     * @param command
     * @return
     */
    public static Class<? extends Step> getStepClass(String command) {
        return getStepClass(Command.byName(command));
    }

    /**
     * Give Step class related to the command.
     * 
     * @param command
     * @return
     */
    public static Class<? extends Step> getStepClass(Command command) {
        switch (command) {
        case ONTO_ELEM:
            return OntoElemStep.class;

        case VALUE_OF:
            return ValueOfStep.class;

        default:
            throw new RuntimeException("Unknown step command: " + command);
        }
    }

    // -------------------------------------------------------------------------

    private Command command;

    // -------------------------------------------------------------------------

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    // -------------------------------------------------------------------------

}
