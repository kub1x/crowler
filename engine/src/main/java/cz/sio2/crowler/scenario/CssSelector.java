/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author kub1x
 *
 */
public class CssSelector extends Selector {

    /**
     * Constructor.
     * 
     * @param value
     */
    CssSelector(String value) {
        super(value);
    }

    /**
     * Get By implementation for WebDriver.
     */
    @Override
    public By getBy() {
        return By.cssSelector(this.getValue());
    }

    /**
     * @param context
     * @return
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
     * NOTE: CSS selectors don't target attributes, so we don't need to handle that here as we do in {@link XPathSelector}.
     */
    @Override
    public String getText(WebElement context) {
        return this.findElement(context).getText();
    }

}
