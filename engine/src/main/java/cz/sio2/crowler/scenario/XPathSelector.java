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
        return this.getValue().replaceAll("/@[^/]+$", "");
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
        return By.xpath(this.getElementSelector());
    }

    /**
     * 
     */
    @Override
    public WebElement findElement(WebElement context) {
        return context.findElement(this.getBy());
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
