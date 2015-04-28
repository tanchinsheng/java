/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q42;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author cstan
 */
public class Test {

    public static void main(String[] args) {
        Map<Integer, String> map = new TreeMap<>();
        map.put(5, "5");
        map.put(10, "10");
        map.put(3, "3");
        map.put(5, "25");
        System.out.println(map);
    }

}
