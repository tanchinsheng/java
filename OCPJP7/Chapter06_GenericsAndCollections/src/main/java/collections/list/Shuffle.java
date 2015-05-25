/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Shuffle {

    public static void main(String[] args) {
        String[] myArgs = {"i", "came", "i", "saw", "i", "left"};
        List<String> list = new ArrayList<>();
        for (String a : myArgs) {
            list.add(a);
        }
        Collections.shuffle(list, new Random());
        System.out.println(list);

        List<String> nextList = Arrays.asList(myArgs);
        Collections.shuffle(nextList);
        System.out.println(list);

    }
}
