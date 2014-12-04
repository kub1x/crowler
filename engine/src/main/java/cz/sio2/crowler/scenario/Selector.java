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
public abstract class Selector {

    private String value;

    /**
     * Constructor.
     * 
     * @param value
     */
    public Selector(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    // -------------------------------------------------------------------------

    public abstract By getBy();

    public abstract WebElement findElement(WebElement context);

    public abstract List<WebElement> findElements(WebElement context);

    public abstract String getText(WebElement context);

}
