/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ran {

    public static void main(String[] args) {

        String[] myArgs = {"i", "came", "i", "saw", "i", "left"};
        // Get and shuffle the list of arguments
        List<String> argList = Arrays.asList(myArgs);
        Collections.shuffle(argList);

        // Print out the elements using JDK 8 Streams
        argList.stream()
                .forEach(e -> System.out.format("%s ", e));

        // Print out the elements using for-each
        for (String arg : argList) {
            System.out.format("%s ", arg);
        }

        System.out.println();
    }
}
