/**
 * 
 */
package cz.sio2.crowler.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author kub1x
 *
 */
public class WebContext {

    private final WebDriver wd;

    public WebContext() {
        this.wd = new FirefoxDriver();
    }

    public void init(String url) {
        // this.wd.manage().window().maximize();
        this.wd.navigate().to(url);
    }

    public void close() {
        this.wd.close();
    }

    public WebElement getBody() {
        return this.wd.findElement(By.tagName("body"));
    }
    // this.wd.get("url");
    //
    // List<WebElement> list = new ArrayList<>();
    //
    // String id = "";
    // list = wd.findElements(By.id(id));
    //
    // String xpath = "";
    // list = wd.findElements(By.xpath(xpath));
    //
    // String selector = "";
    // list = wd.findElements(By.cssSelector(selector));
}
