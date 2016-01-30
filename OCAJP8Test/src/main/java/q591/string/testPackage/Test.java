/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q591.string.testPackage;

import q591.string.other.*;

/**
 *
 * What will be the output of running class Test?
 */
class Other {

    static String hello = "Hello";
}

class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String hello = "Hello", lo = "lo";
        System.out.print((q591.string.testPackage.Other.hello == hello) + " ");    //line 1:true
        System.out.print((q591.string.other.Other.hello == hello) + " ");   //line 2:true
        System.out.print((hello == ("Hel" + "lo")) + " ");           //line 3:true
        System.out.print((hello == ("Hel" + lo)) + " ");              //line 4:false
        System.out.println(hello == ("Hel" + lo).intern());          //line 5:true
    }
}
