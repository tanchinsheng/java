/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.util.concurrent.TimeoutException;
import junit.framework.TestCase;

/**
 ** Sample JUnit 3 test class.
 * 
 * It must be a (direct or indirect) subclass of class
 * {@code junit.framework.TestCase}.
 * 
 * @author nb
 */
public class UtilsJUnit3Test extends TestCase {

    public UtilsJUnit3Test(String testName) {
        super(testName);
    }

    
    /**
     * Test initialization method. It is executed before each test method
     * of this test class.
     * The test initialization method is recognized by the following attributes:
     *   its name is &quot;{@code setUp}&quot;,
     *   it has no arguments,
     *   it has {@void} return type and
     *   it is protected or public.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("* UtilsJUnit3Test: setUp() method");
    }

    
    /**
     * Test initialization method. It is executed after each test method
     * of this test class.
     * The test shutdown method is recognized by the following attributes:
     *   its name is &quot;{@code tearDown}&quot;,
     *   it has no arguments,
     *   it has {@void} return type and
     *   it is protected or public.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.out.println("* UtilsJUnit3Test: tearDown() method");
    }

    /**
     * Test of concatWords method, of class Utils.
     */
    public void testHelloWorld() {
        System.out.println("* UtilsJUnit3Test: test method 1 - testHelloWorld()");
        assertEquals("Hello, world!", Utils.concatWords("Hello", ", ", "world", "!"));
    }

    /**
     * Test with timeout. If the testing routine does not finish in one second,
     * it is interrupted and a {@code TimeoutException} is thrown.
     * <i>JUnit 4 has special support for this kind of tests - the timeout may
     * be specified by an annotation of the test method.</i>
     */
    public void testWithTimeout() throws InterruptedException, TimeoutException {
        System.out.println("* UtilsJUnit3Test: test method 2 - testWithTimeout()");
        final int factorialOf = 1 + (int) (30000 * Math.random());
        System.out.println("computing " + factorialOf + '!');

        Thread testThread = new Thread() {

            public void run() {
                System.out.println(factorialOf + "! = " + Utils.computeFactorial(factorialOf));
            }
        };

        testThread.start();
        Thread.sleep(1000);
        testThread.interrupt();

        if (testThread.isInterrupted()) {
            throw new TimeoutException("the test took too long to complete");
        }
    }

    
    /**
     * Test of expected exception. If a given exception is not thrown during the
     * tested routine, the test fails. If the exception is thrown, the test
     * passes.
     * <i>JUnit 4 has special support for this kind of tests - the fact that an
     * exception is expected may be declared by an annotation of the test
     * method.</i>
     */
    public void testExpectedException() {
        System.out.println("* UtilsJUnit3Test: test method 3 - testExpectedException()");
        try {
            final int factorialOf = -5;
            System.out.println(factorialOf + "! = " + Utils.computeFactorial(factorialOf));
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException ex) {
        }
    }

    
    /**
     * Test that is temporarily disabled. It is disabled by prepending prefix
     * DISABLED_ in front of the original test method's name. The prefix changes
     * name of the test method such that it does not begin with
     * &quot;test&quot; - the result is that it is not executed.
     * <i>JUnit 4 tests can be disabled simply by adding an {@code @Ignore}
     * annotation to the test method.</i>
     */
    public void DISABLED_testTemporarilyDisabled() throws Exception {
        System.out.println("* UtilsJUnit3Test: test method 4 - checkExpectedException()");
        assertEquals("Malm\u00f6", Utils.normalizeWord("Malmo\u0308"));
    }
}
