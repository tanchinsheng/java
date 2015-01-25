/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 ** Sample JUnit 4 test class.
 * 
 * Note that it does not need to be a subclass of any particular class.
 * 
 * @author nb
 */
public class UtilsJUnit4Test {

    public UtilsJUnit4Test() {
    }

    
    /**
     * Initialization method that is called only once. It is run before
     * any test method is executed. It is run even sooner than any
     * {@code @Before} method. If a test class is a test suite, its
     * {@code @BeforeClass} method is called even sooner than any of test
     * classes contained in the suite. <em>But:</em> If any of superclasses
     * of a test class has some {@code @BeforeClass} method, the superclass
     * {@code @BeforeClass} is called before. There may be only one method
     * annotated with the {@code @BeforeClass} annotation.
     * The name of the method is irrelevant - the only mandatory attributes are:
     *   the {@code @BeforeClass} annotation,
     *   the {@code public} and {@code static} modifiers
     *   {@void} return type and
     *   no arguments.
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        /*
         * E.g. establish connection to a database so that it does not need to
         * be established for each test separately.
         */
        System.out.println("* UtilsJUnit4Test: @BeforeClass method");
    }

    
    /**
     * Tear down method that is called only once. It is run after all test
     * methods and their {@code @After} methods are executed.
     * If a test class is a test suite, its {@code @AfterClass} method is called
     * even only after all test classes contained in the suite finish running.
     * <em>But:</em> If any of the superclasses of a test class has some
     * {@code @BeforeClass} method, the superclass {@code @BeforeClass} is
     * called later. There may be only one method annotated with the
     * {@code @AfterClass} annotation.
     * The name of the method is irrelevant - the only mandatory attributes are:
     *   the {@code @AfterClass} annotation,
     *   the {@code public} and {@code static} modifiers
     *   {@void} return type and
     *   no arguments.
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        /*
         * E.g. disconnect from the database - i.e. the complement to the action
         * performed in setUpClass().
         */
        System.out.println("* UtilsJUnit4Test: @AfterClass method");
    }

    
    /**
     * Test initialization method. It is executed before each test method
     * of this test class. There may be multiple test initialization methods
     * - in such a case, all these methods are executed before each test method
     * but order of their execution is undefined. The name of the method is
     * irrelevant - the only mandatory attributes are:
     *   the {@code @Before} annotation,
     *   the {@code public} access modifier
     *   the {@void} return type and
     *   no arguments.
     */
    @Before
    public void setUp() {
        System.out.println("* UtilsJUnit4Test: @Before method");
    }

    
    /**
     * Test shutdown method. It is executed after each test method
     * of this test class. There may be multiple test shutdown methods
     * - in such a case, all these methods are executed after each test method
     * but order of their execution is undefined. The name of the method is
     * irrelevant - the only mandatory attributes are:
     *   the {@code @After} annotation,
     *   the {@code public} access modifier
     *   the {@void} return type and
     *   no arguments.
     */
    @After
    public void tearDown() {
        System.out.println("* UtilsJUnit4Test: @After method");
    }

    /**
     * Test of concatWords method, of class Utils.
     * Simple test method. It does not need to follow any naming conventions,
     * just the {@code @Test} annotation is important.
     */
    @Test
    public void helloWorldCheck() {
        System.out.println("* UtilsJUnit4Test: test method 1 - helloWorldCheck()");
        assertEquals("Hello, world!", Utils.concatWords("Hello", ", ", "world", "!"));
    }

    /**
     * Test with timeout. If the testing routine does not finish in one second,
     * it is interrupted and the test fails. The timeout is declared by an
     * argument to the {@code @Test} annotation.
     * 
     */
    @Test(timeout = 1000)
    public void testWithTimeout() {
        System.out.println("* UtilsJUnit4Test: test method 2 - testWithTimeout()");
        final int factorialOf = 1 + (int) (30000 * Math.random());
        System.out.println("computing " + factorialOf + '!');
        System.out.println(factorialOf + "! = " + Utils.computeFactorial(factorialOf));
    }

    /**
     * Test of expected exception. If a given exception is not thrown during the
     * tested routine, the test fails. If the exception is thrown, the test
     * passes. The expected annotation is declared by an argument to the
     * {@code @Test} annotation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkExpectedException() {
        System.out.println("* UtilsJUnit4Test: test method 3 - checkExpectedException()");
        final int factorialOf = -5;
        System.out.println(factorialOf + "! = " + Utils.computeFactorial(factorialOf));
    }

    /**
     * Test that is temporarily disabled simply by the special {@code @Ignore}
     * annotation.
     */
    @Ignore
    @Test
    public void temporarilyDisabledTest() throws Exception {
        System.out.println("* UtilsJUnit4Test: test method 4 - checkExpectedException()");
        assertEquals("Malm\u00f6", Utils.normalizeWord("Malmo\u0308"));
    }
}