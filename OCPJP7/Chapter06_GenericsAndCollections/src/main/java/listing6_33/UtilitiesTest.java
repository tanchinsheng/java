/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing6_33;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cstan
 */
public class UtilitiesTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<Integer> intList = new LinkedList<>();
        List<Double> dblList = new LinkedList<>();
        System.out.println("First Type: " + intList.getClass());
        System.out.println("Second Type: " + dblList.getClass());
    }

}
