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
public class Q20 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("one");
        list.add("two");
        list.add(7);
        for (String s : list) {
            System.out.print(s);
        }
    }

}
