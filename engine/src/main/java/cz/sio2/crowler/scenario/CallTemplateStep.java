/**
 * 
 */
package cz.sio2.crowler.scenario;

/**
 * @author kub1x
 *
 */
public class CallTemplateStep extends Step {

    private String templateName;
    private String url;

    /**
     * Constructor.
     */
    public CallTemplateStep() {
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
