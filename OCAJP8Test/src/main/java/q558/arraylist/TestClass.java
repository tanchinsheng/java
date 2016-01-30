/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q558.arraylist;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * What will the following code print when compiled and run?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List al = new ArrayList(); //1
        al.add(111); //2
        System.out.println(al.get(al.size()));  //3
    }

}
