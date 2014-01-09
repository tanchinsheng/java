/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sortinglist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class StringLengthComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (len1 > len2) {
            return 1;
        } else if (len1 < len2) {
            return -1;
        }

        return 0;

    }
}

class AphhabeticalComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);

    }
}

class ReverseComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        return -s1.compareTo(s2);

    }
}

class Person {

    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + '}';
    }
}

/**
 *
 * @author cstan
 */
public class SortingList {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<String> animals = new ArrayList<String>();

        animals.add("tiger");
        animals.add("lion");
        animals.add("cat");
        animals.add("snake");
        animals.add("mongoose");
        animals.add("elephant");

        // Collections.sort(animals, new StringLengthComparator());
        Collections.sort(animals, new AphhabeticalComparator());
        Collections.sort(animals, new ReverseComparator());
        for (String animal : animals) {
            System.out.println(animal);
        }

        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(3);
        numbers.add(36);
        numbers.add(73);
        numbers.add(40);
        numbers.add(1);

        Collections.sort(numbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer num1, Integer num2) {
                return -num1.compareTo(num2);
            }
        });

        for (Integer number : numbers) {
            System.out.println(number);
        }

        //Sort objects
        List<Person> people = new ArrayList<Person>();
        people.add(new Person(1, "Joe"));
        people.add(new Person(3, "Sob"));
        people.add(new Person(4, "Clare"));
        people.add(new Person(2, "Sue"));

        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                if (p1.getId() > p2.getId()) {
                    return 1;
                } else if (p1.getId() < p2.getId()) {
                    return -1;
                }
                return 0;
            }
        });

        for (Person person : people) {
            System.out.println(person);
        }
        //Sort in name


        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });
        
        for (Person person : people) {
            System.out.println(person);
        }

    }
}
