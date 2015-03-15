/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Diamond {

    public static void main(String[] args) {
        List list1 = new ArrayList<>(Arrays.asList(1, "two", 3.0)); // ONE
        List list2 = new LinkedList<>(Arrays.asList(new Integer(1), new Float(2.0F), new Double(3.0))); // TWO
        list1 = list2; // THREE
        for (Object element : list1) {
            System.out.print(element + " ");
        }
    }
}
