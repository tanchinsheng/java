/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q38;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Test {

    public static void main(String[] args) {
        Set<Integer> set = new LinkedHashSet<Integer>();
        LinkedHashSet<Integer> set2 = new HashSet<Integer>();// HashSet cannot be converted to LinkedHashSet
        SortedSet<Integer> set3 = new TreeSet<Integer>();
        SortedSet<Integer> set4 = new NavigableSet<Integer>();// Navigable Set is abstract
    }

}
