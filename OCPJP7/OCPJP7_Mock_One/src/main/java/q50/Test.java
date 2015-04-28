/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q50;

import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.NavigableMap;

/**
 *
 * @author cstan
 */
public class Test {

    public static void main(String[] args) {
        Map<String, String> map1 = new NavigableMap<>();//interface
        Map<String, String> map2 = new IdentityHashMap<>();
        Map<String, String> map3 = new Hashtable<>();
        Map<String, String> map4 = new ConcurrentMap<>();//interface
    }

}
