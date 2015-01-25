/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;

/**
 *
 * @author cstan
 */
public class UtilsTest {

    public UtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("* UtilsTest: @BeforeClass method");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("* UtilsTest: @AfterClass method");
    }

    @Before
    public void setUp() {
        System.out.println("* UtilsTest: @Before method");
    }

    @After
    public void tearDown() {
        System.out.println("* UtilsTest: @After method");
    }

    /**
     * Test of concatWords method, of class Utils.
     */
    @Test
    public void helloWorldCheck() {
        System.out.println("* UtilsTest: test method 1 - helloWorldCheck()");
        assertEquals("Hello, world!", Utils.concatWords("Hello", ", ", "world", "!"));
    }

    /**
     * Test of computeFactorial method, of class Utils.
     */
    @Test(timeout = 5000)
    public void testWithTimeout() {
        System.out.println("* UtilsTest: test method 2 - testWithTimeout()");
        final int factorialOf = 1 + (int) (30000 * Math.random());
        System.out.println("computing " + factorialOf + '!');
        System.out.println(factorialOf + "! = " + Utils.computeFactorial(factorialOf));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkExpectedException() {
        System.out.println("* UtilsTest: test method 3 - checkExpectedException()");
        final int factorialOf = -5;
        System.out.println(factorialOf + "! = " + Utils.computeFactorial(factorialOf));
    }

    /**
     * Test of normalizeWord method, of class Utils.
     */
    @Ignore // FOR DEMO, REMOVE!
    @Test
    public void temporarilyDisabledTest() throws Exception {
        System.out.println("* UtilsJUnit4Test: test method 4 - temporarilyDisabledTest()");
        assertEquals("Malm\u00f6", Utils.normalizeWord("Malmo\u0308"));
    }
}
