/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q551.arraylist;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * What will the following code snippet print?
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List s1 = new ArrayList();
        try {
            while (true) {
                s1.add("sdfa");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        System.out.println(s1.size());
    }

}
