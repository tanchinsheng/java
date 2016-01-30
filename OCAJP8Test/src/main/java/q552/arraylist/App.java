/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q552.arraylist;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List s1 = new ArrayList();
        s1.add("a");
        s1.add("b");
        s1.add("c");
        s1.add("a");
        System.out.println(s1.remove("a") + " " + s1.remove("x"));
    }

}
