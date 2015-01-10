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
public class WebContext implements AutoCloseable {

    private final WebDriver wd;

    public WebContext() {

        // String phantomPath = System.getProperty("phantom.path");
        // if (phantomPath == null) {
        // Firefox
        this.wd = new FirefoxDriver();
        // } else {
        // // Phantom
        // DesiredCapabilities caps = new DesiredCapabilities();
        // caps.setJavascriptEnabled(true); // not really needed: JS enabled by default
        // caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomPath);
        // this.wd = new PhantomJSDriver(caps);
        // }
        // HtmlUint
        // this.wd = new HtmlUnitDriver(BrowserVersion.FIREFOX_);
    }

    public void goTo(String url) {
        this.wd.navigate().to(url);
    }

    public void back() {
        this.wd.navigate().back();
    }

    public void close() {
        this.wd.close();
        this.wd.quit();
    }

    public WebElement getBody() {
        return this.wd.findElement(By.tagName("body"));
    }

}
