/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q429.inheritance;

/**
 *
 * What will happen when the above code is compiled and run?
 */
public class Sample implements IInt {

    /**
     * As a rule, fields defined in an interface are public, static, and final.
     * The methods are public. Here, the interface IInt defines thevalue and
     * thus any class that implements this interface gets this field. Therefore,
     * it can be accessed using s.thevalue or just thevalue inside the class.
     * Also, since it is static, it can also be accessed using IInt.thevalue or
     * Sample.thevalue.
     */
    public static void main(String[] args) {

        Sample s = new Sample();//1       
        int j = s.thevalue;//2       
        int k = IInt.thevalue;//3
        int l = thevalue;//4
    }

}
