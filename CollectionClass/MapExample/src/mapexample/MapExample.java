/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapexample;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;




class Person {
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String toString() {
        return "ID is " + id + ", name is " + name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.id;
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        return true;
    }
    
    
}

/**
 *
 * @author cstan
 */
public class MapExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // Key unique
        
        Person p1 = new Person(0, "Bob"); //id, name
        Person p2 = new Person(1, "Sue");
        Person p3 = new Person(2, "Mike");
        Person p4 = new Person(1, "Sue");
                
        Map<Person, Integer> map = new LinkedHashMap<Person, Integer>();
        map.put(p1, 1);
        map.put(p2, 2);
        map.put(p3, 3);
        map.put(p4, 1);
        
        for (Person key: map.keySet()) {
            System.out.println(key + ":" + map.get(key)); //value
        }
        
        Set<Person> set = new LinkedHashSet<Person>();
        set.add(p1);
        set.add(p2);
        set.add(p3);
        set.add(p4);
        
        System.out.println(set);
        
    }
}
