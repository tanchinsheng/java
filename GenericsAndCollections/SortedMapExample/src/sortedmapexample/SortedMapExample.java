/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sortedmapexample;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author cstan
 */
public class SortedMapExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Map<Integer, String> hashMap = new HashMap<Integer, String>();
        Map<Integer, String> linkedHashMap = new LinkedHashMap<Integer, String>();
        Map<Integer, String> treeMap = new TreeMap<Integer, String>();
        
        testMap("hashMap", hashMap);
        testMap("linkedHashMap", linkedHashMap);
        testMap("treeMap", treeMap);
    }
    
    public static void testMap(String type, Map<Integer, String> map) {
        map.put(9, "fox");
        map.put(4, "cat");
        map.put(8, "dog");
        map.put(1, "giraafe");
        map.put(0, "swan");
        map.put(15, "bear");
        map.put(6, "snake");
        
        for (Integer key: map.keySet()){
            String value = map.get(key);
            System.out.println(type + "," + key + ":" + value);
        }
        
    }
}
