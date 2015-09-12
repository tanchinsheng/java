/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q123.javadatatypes.gc;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * What can be inserted at // 1, which will make the object referred to by
     * obj eligible for garbage collection?
     */
    void test() {
        MyClass obj = new MyClass();
        obj.name = "jack";
        // 1 insert code here   
        obj = null; // GC
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
