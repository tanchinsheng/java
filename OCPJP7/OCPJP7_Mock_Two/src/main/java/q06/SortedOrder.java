/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q06;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class SortedOrder {

    public static void main(String[] args) {
        Set<String> set = new TreeSet<>();
        set.add("S");
        set.add("R");
        Iterator<String> iter = set.iterator();
        set.add("P");
        set.add("Q");
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

}
