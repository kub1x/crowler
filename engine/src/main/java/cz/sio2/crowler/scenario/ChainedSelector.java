/**
 * 
 */
package cz.sio2.crowler.scenario;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Calling multiple selectors one by one.
 * 
 * @author kub1x
 *
 */
public class ChainedSelector extends Selector {

    private List<Selector> selectors = new LinkedList<>();

    /**
     * Constructor.
     * 
     * @param selectors
     *            in their chaining order (wider to narrower).
     */
    public ChainedSelector(Selector... selectors) {
        for (Selector s : selectors) {
            this.selectors.add(s);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.sio2.crowler.scenario.Selector#getBy()
     */
    @Override
    public By getBy() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.sio2.crowler.scenario.Selector#findElement(org.openqa.selenium.WebElement)
     */
    @Override
    public WebElement findElement(WebElement context) {
        WebElement result = context;
        for (Selector s : this.selectors) {
            result = s.findElement(result);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.sio2.crowler.scenario.Selector#findElements(org.openqa.selenium.WebElement)
     */
    @Override
    public List<WebElement> findElements(WebElement context) {
        List<WebElement> prev = new ArrayList<>();
        prev.add(context);
        // Initialized as list containing context...
        List<WebElement> result = prev;
        for (Selector s : this.selectors) {
            result = new ArrayList<>();
            for (WebElement e : prev) {
                result.addAll(s.findElements(e));
            }
            prev = result;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.sio2.crowler.scenario.Selector#getText(org.openqa.selenium.WebElement)
     */
    @Override
    public String getText(WebElement context) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Add selector at the end of selector chain.
     * 
     * @param selector
     */
    public void addSelector(Selector selector) {
        this.selectors.add(selector);
    }

    /**
     * Add selector to a specified position.
     * 
     * @param index
     * @param selector
     */
    public void addSelector(int index, Selector selector) {
        this.selectors.add(index, selector);
    }

}
