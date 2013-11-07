/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part4sortedmaps;

/**
A tutorial on sorted maps in Java, plus some explanation of the Map interface and interfaces in general
 */
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
 
public class App {
 
    public static void main(String[] args) {
        Map<Integer, String> hashMap = new HashMap<>();//no order, non-synchronized
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();// keep order
        Map<Integer, String> treeMap = new TreeMap<>();// natural order...1,2,3,4...a,b,c
         
        testMap(hashMap, "hashMap");
        testMap(linkedHashMap, "linkedHashMap");
        testMap(treeMap, "treeMap");
    }
     
    public static void testMap(Map<Integer, String> map, String type) {
        map.put(9, "fox");
        map.put(4, "cat");
        map.put(8, "dog");
        map.put(1, "giraffe");
        map.put(0, "swan");
        map.put(15, "bear");
        map.put(6, "snake");
         
        for(Integer key: map.keySet()) {// iterate over keys
            String value = map.get(key);    
            System.out.println(type + ", " + key + ": " + value);
        }
        
            //hashMap, 0: swan
            //hashMap, 1: giraffe
            //hashMap, 4: cat
            //hashMap, 6: snake
            //hashMap, 8: dog
            //hashMap, 9: fox
            //hashMap, 15: bear
            //linkedHashMap, 9: fox
            //linkedHashMap, 4: cat
            //linkedHashMap, 8: dog
            //linkedHashMap, 1: giraffe
            //linkedHashMap, 0: swan
            //linkedHashMap, 15: bear
            //linkedHashMap, 6: snake
            //treeMap, 0: swan
            //treeMap, 1: giraffe
            //treeMap, 4: cat
            //treeMap, 6: snake
            //treeMap, 8: dog
            //treeMap, 9: fox
            //treeMap, 15: bear
        
    }
     
}