/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections.set;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FindDups {

    public static void main(String[] args) {
        String[] myArgs = {"i", "came", "i", "saw", "i", "left"};
        Set<String> distinctWords = Arrays.asList(myArgs).stream().collect(Collectors.toSet());
        System.out.println(distinctWords.size() + " distinct words: " + distinctWords);

        Set<String> s = new HashSet<>();
        for (String a : myArgs) {
            s.add(a);
        }
        System.out.println(s.size() + " distinct words: " + s);

    }

}
