/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q456.inheritance;

import static org.testng.Assert.*;

/**
 *
 * @author cstan
 */
public class SoccerNGTest {

    public SoccerNGTest() {
    }

    @org.testng.annotations.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.testng.annotations.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.testng.annotations.BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @org.testng.annotations.AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of play method, of class Soccer.
     */
    @org.testng.annotations.Test
    public void testPlay() {
        System.out.println("play");
        Soccer instance = new Soccer();
        instance.play();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Soccer.
     */
    @org.testng.annotations.Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Soccer.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
