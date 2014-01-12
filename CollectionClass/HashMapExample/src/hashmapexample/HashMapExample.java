/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hashmapexample;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cstan
 */
public class HashMapExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap<Integer, String> map= new HashMap<Integer, String>();
        
        map.put(5, "Five");
        map.put(8, "Eight");
        map.put(4, "Four");
        map.put(2, "Two");
        
        String text = map.get(5);
        System.out.println(text);
        
        for (Map.Entry<Integer, String> entry: map.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ":" + value);
        }
        
    }
}
