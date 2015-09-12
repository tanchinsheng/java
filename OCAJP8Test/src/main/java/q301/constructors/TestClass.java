/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q301.constructors;

/**
 *
 * What will be the result of attempting to compile the following program?
 */
public class TestClass {

    /**
     * The declaration at (1) declares a method, not a constructor because it
     * has a return value. The method happens to have the same name as the
     * class, but that is ok. The class has an implicit default constructor
     * since the class contains no constructor declarations. This allows the
     * instantiation at (2) to work.
     */
    long l1;

    public void TestClass(long pLong) {
        l1 = pLong;
    }  //(1)

    public static void main(String args[]) {
        TestClass a, b;
        a = new TestClass();  //(2)
        b = new TestClass(5);  //(3)
    }

}
