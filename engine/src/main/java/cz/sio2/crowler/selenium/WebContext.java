/**
 * 
 */
package cz.sio2.crowler.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author kub1x
 *
 */
public class WebContext implements AutoCloseable {

    private final WebDriver wd;

    public WebContext() {
        // this.wd = new FirefoxDriver();
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true); // not really needed: JS enabled by default
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:/Program Files/phantomjs-1.9.8/phantomjs.exe");
        this.wd = new PhantomJSDriver(caps);
    }

    public void goTo(String url) {
        this.wd.navigate().to(url);
    }

    public void close() {
        this.wd.close();
        this.wd.quit();
    }

    public WebElement getBody() {
        return this.wd.findElement(By.tagName("body"));
    }

}
