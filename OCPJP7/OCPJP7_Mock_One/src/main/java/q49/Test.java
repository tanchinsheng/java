/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q49;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Integer> intList = new ArrayList<>();
        intList.add(10);
        intList.add(20);
        List list = intList;
        list.add("hello");
        for (Object o : list) {
            System.out.println(o + " ");
        }
    }

}
