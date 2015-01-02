package cz.sio2.crowler.scenario;

/**
 * @author kub1x
 *
 */
public class CallTemplateStep extends Step {

    private String templateName;
    private String url;
    private Selector selector;
    private ValueOfStep valueOfStep;

    // -------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public CallTemplateStep() {
        this.setCommand(Command.CALL_TEMPLATE);
    }

    // -------------------------------------------------------------------------

    /**
     * @return the template name
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName
     *            the template name to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param URL
     *            the URL to set
     */
    public void setUrl(String url) {
        this.url = url;
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

    /**
     * @return the valueOfStep
     */
    public ValueOfStep getValueOfStep() {
        return valueOfStep;
    }

    /**
     * @param valueOfStep
     *            the valueOfStep to set
     */
    public void setValueOfStep(ValueOfStep valueOfStep) {
        this.valueOfStep = valueOfStep;
    }

}
