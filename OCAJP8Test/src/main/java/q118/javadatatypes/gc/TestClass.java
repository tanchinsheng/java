/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q118.javadatatypes.gc;

/**
 *
 * @author cstan
 */
class MyClass {
}

public class TestClass {

    MyClass getMyClassObject() {
        MyClass mc = new MyClass(); //1
        return mc; //2
    }

    /**
     * At line 6, x starts pointing to a new MyClassObject and no reference to
     * the original MyClass object is left.
     */
    public static void main(String[] args) {
        TestClass tc = new TestClass(); //3
        MyClass x = tc.getMyClassObject(); //4       
        System.out.println("got myclass object"); //5       
        x = new MyClass(); //6       
        System.out.println("done"); //7    
    }

}
