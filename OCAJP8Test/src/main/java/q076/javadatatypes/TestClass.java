/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q076.javadatatypes;

/**
 *
 * A declared reference variable exists regardless of whether a reference value
 * (i.e. an object) has been assigned to it or not.
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestClass t1, t2, t3, t4;
        t1 = t2 = new TestClass();
        t3 = new TestClass();
        //two news => two objects. t1, t2, t3, t4 => 4 references.
    }

}
