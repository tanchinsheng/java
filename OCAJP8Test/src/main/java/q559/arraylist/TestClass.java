/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q559.arraylist;

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

        List list = new ArrayList();
        list.add("val1"); //1         
        list.add(2, "val2"); //2         
        list.add(1, "val3"); //3         
        System.out.println(list);
    }

}
