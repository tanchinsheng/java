/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q122.javadatatypes.gc;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * After which line will the object created at line XXX be eligible for
     * garbage collection?
     */
    public Object getObject() {
        Object obj = new String("aaaaa");//1
        Object objArr[] = new Object[1]; //2      
        objArr[0] = obj; //3
        obj = null;//4  
        objArr[0] = null;//5
        return obj;//6 GC
    }

    public static void main(String[] args) {
        TestClass o = new TestClass();
        o.getObject();
    }

}
