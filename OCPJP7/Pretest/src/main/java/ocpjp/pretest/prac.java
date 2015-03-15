/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class prac {

    public static void find(String value, Map m1) {

        Set s = m1.entrySet();

        Iterator i = s.iterator();

        String Key;
        String Value;
        Map.Entry m;
        while (i.hasNext()) {
            m = (Map.Entry) i.next();
            if (m.getValue().equals(value)) {
                System.out.println(m.getKey());
            }

            Key = m.getKey().toString();
            Value = m.getValue().toString();

            m1.remove(m.getKey());
            m1.put(Value, Key);


        }

        System.out.println("after swapping: " + m1);

    }

    public static void main(String args[]) {
        Map m1 = new ConcurrentHashMap();

        m1.put("1", "aa");
        m1.put("2", "bb");
        m1.put("3", "cc");

        System.out.println("before swapping: " + m1);
        find("cc", m1);

    }
}