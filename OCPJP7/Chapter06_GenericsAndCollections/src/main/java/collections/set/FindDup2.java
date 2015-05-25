/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections.set;

import java.util.HashSet;
import java.util.Set;

public class FindDup2 {

    public static void main(String[] args) {

        String[] myArgs = {"i", "came", "i", "saw", "i", "left"};
        Set<String> uniques = new HashSet<>();
        Set<String> dups = new HashSet<>();

        for (String a : myArgs) {
            if (!uniques.add(a)) {
                dups.add(a);
            }
        }

        uniques.removeAll(dups);
        System.out.println("Unique words: " + uniques);
        System.out.println("Duplicate words " + dups);

        {

        }
    }

}
