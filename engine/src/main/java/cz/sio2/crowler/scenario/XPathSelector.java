/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author kub1x
 *
 */
public class XPathSelector extends Selector {
    private final static Pattern ATTR_PAT = Pattern.compile("^.*@([^/]+)$");

    /**
     * Constructor.
     * 
     * @param value
     */
    public XPathSelector(String value) {
        super(value);
    }

    /**
     * 
     * @return
     */
    public boolean isAttributeSelector() {
        return this.getValue().matches("^.*@[^/]+$");
    }

    /**
     * 
     * @return
     */
    public String getAttributeName() {
        Matcher matcher = ATTR_PAT.matcher(this.getValue());
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            // TODO don't do this dude!!!
            return null;
        }
    }

    /**
     * Will just strip the occasional attribute part from the XPath string.
     * 
     * @return
     */
    public String getElementSelector() {
        String selector = this.getValue().replaceAll("/?@[^/]+$", "").trim();
        return (selector.isEmpty() ? null : selector);
    }

    /**
     * 
     */
    @Override
    public String getValue() {
        String value = super.getValue();
        return value;
    }

    /**
     * 
     */
    @Override
    public By getBy() {
        String selector = this.getElementSelector();
        return (selector == null ? null : By.xpath(selector));
    }

    /**
     * NOTE if there is no element selector (by is null) we might be on "attribute only" XPath selector, so we'd better just return the context back.
     */
    @Override
    public WebElement findElement(WebElement context) {
        By by = this.getBy();
        return (by == null ? context : context.findElement(by));
    }

    /**
     * 
     */
    @Override
    public List<WebElement> findElements(WebElement context) {
        return context.findElements(this.getBy());
    }

    /**
     * 
     */
    @Override
    public String getText(WebElement context) {
        WebElement node = this.findElement(context);
        if (this.isAttributeSelector()) {
            return node.getAttribute(this.getAttributeName());
        } else {
            return node.getText();
        }
    }

}
