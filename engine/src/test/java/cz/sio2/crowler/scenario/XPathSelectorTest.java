/**
 * 
 */
package cz.sio2.crowler.scenario;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author kub1x
 *
 */
public class XPathSelectorTest {

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

    private String selector = "//node/@attribute";
    private XPathSelector xps = new XPathSelector(selector);

    @Test
    public void testAttribute() {
        assertEquals("attribute", xps.getAttributeName());
    }

    @Test
    public void testNode() {
        assertEquals("//node", xps.getElementSelector());
    }

}
