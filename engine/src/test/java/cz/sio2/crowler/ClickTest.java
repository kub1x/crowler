/**
 * 
 */
package cz.sio2.crowler;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cz.sio2.crowler.selenium.WebContext;

/**
 * @author kub1x
 *
 */
public class ClickTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        WebContext wc = new WebContext();
        wc.goTo("http://www.inventati.org/kub1x/t/");
        WebElement ctx = wc.getBody();
        WebElement a = ctx.findElement(By.cssSelector("a"));
        a.click();
        wc.back();
        System.out.println(a.getText());
        System.out.println(wc.getBody().getText());
        // fail("Not yet implemented");
    }

}
