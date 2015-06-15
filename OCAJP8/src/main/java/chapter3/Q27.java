/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cstan
 */
public class Q27 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<String> one = new ArrayList<>();
        one.add("abc");
        List<String> two = new ArrayList<>();
        two.add("abc");
        if (one == two) {
            System.out.println("A");
        } else if (one.equals(two)) {
            System.out.println("B");
        } else {
            System.out.println("C");
        }
    }

}
