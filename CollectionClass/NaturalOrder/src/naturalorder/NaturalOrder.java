/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package naturalorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

class Person implements Comparable<Person> {

    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    @Override
//    public int compareTo(Person person) {
//        return name.compareTo(person.name);
//    }
    public int compareTo(Person person) {
        
        int len1 = name.length();
        int len2 = person.name.length();
        
        if (len1>len2) {
            return 1;
        }
        
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.name != null ? this.name.hashCode() : 0);
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
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}

/**
 *
 * @author cstan
 */
public class NaturalOrder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<Person> list = new ArrayList<Person>();

        //same to Treemap
        //put sorted set just a reminder
        SortedSet<Person> set = new TreeSet<Person>();

        addElements(list);
        addElements(set);
        Collections.sort(list);
        System.out.println();
        showElements(list);
        showElements(set);

    }

    private static void addElements(Collection<Person> col) {
        col.add(new Person("Joe"));
        col.add(new Person("Sue"));
        col.add(new Person("Julient"));
        col.add(new Person("Clare"));
        col.add(new Person("Mike"));
    }

    private static void showElements(Collection<Person> col) {
        for (Person element : col) {
            System.out.println(element);
        }
    }
}
