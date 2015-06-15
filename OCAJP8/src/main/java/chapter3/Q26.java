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
public class Q26 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<Integer> ages = new ArrayList<>();
        ages.add(Integer.parseInt("5"));
        ages.add(Integer.valueOf("6"));
        ages.add(7);
        ages.add(null);
        for (int age : ages) {
            System.out.print(age);
        }
    }

}
