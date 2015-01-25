/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 *
 * @author cstan
 */
public class UtilsNGTest {
    
    public UtilsNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("* UtilsNGTest: @BeforeClass method");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("* UtilsNGTest: @AfterClass method");
    }

    @BeforeMethod
    public void setUp() {
        System.out.println("* UtilsNGTest: @Before method");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("* UtilsNGTest: @After method");
    }
    
    /**
     * Test of concatWords method, of class Utils.
     */
    @Test
    public void helloWorldCheck() {
        System.out.println("* UtilsNGTest: test method 1 - helloWorldCheck()");
        assertEquals("Hello, world!", Utils.concatWords("Hello", ", ", "world", "!"));
    }

    /**
     * Test of computeFactorial method, of class Utils.
     */
    @Test(timeOut= 5000)
    public void testWithTimeout() {
        System.out.println("* UtilsNGTest: test method 2 - testWithTimeout()");
        final int factorialOf = 1 + (int) (30000 * Math.random());
        System.out.println("computing " + factorialOf + '!');
        System.out.println(factorialOf + "! = " + Utils.computeFactorial(factorialOf));
    }

    @Test(expectedExceptions  = IllegalArgumentException.class)
    public void checkExpectedException() {
        System.out.println("* UtilsNGTest: test method 3 - checkExpectedException()");
        final int factorialOf = -5;
        System.out.println(factorialOf + "! = " + Utils.computeFactorial(factorialOf));
    }

    /**
     * Test of normalizeWord method, of class Utils.
     */
    @Test(enabled=false)
    public void temporarilyDisabledTest() throws Exception {
        System.out.println("* UtilsNGTest: test method 4 - temporarilyDisabledTest()");
        assertEquals("Malm\u00f6", Utils.normalizeWord("Malmo\u0308"));
    }
}
