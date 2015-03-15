/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyMap {

    public static void main(String[] args) {
        String val = "value";
        String key = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "map");
        map.put("2", "value");
        System.out.println("Intial Map " + map);
       // if (map.containsValue(val)) {
            Set<Entry<String, String>> s = map.entrySet();
            Iterator<Entry<String, String>> i = s.iterator();
            for (; i.hasNext(); i.next()) {
                Entry<String, String> e = i.next();
                if (e.getValue().equals(val)) {
                    key = e.getKey();
                    System.out.println("The key is " + e.getKey());
                }
            }
        //}
        if (key.length() > 0) {
            map.remove(key);
            map.put(val, key);
        }
        System.out.println("Final Map " + map);
    }
}