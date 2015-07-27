package FilterCollection;

import FilterCollection.model.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PredicateLambda {

    public static void main(String args[]) {

        List<Person> people = new ArrayList<>();

        people.add(new Person("Joe", 48));
        people.add(new Person("Mary", 30));
        people.add(new Person("Mike", 73));

        Predicate<Person> predOlder = (p) -> p.getAge() >= 65;
        Predicate<Person> predYounger = (p) -> p.getAge() <= 40;
        displayPeople(people, predOlder);
        displayPeople(people, predYounger);
    }
//        people.forEach(p -> {
//            if (predOlder.test(p)) {
//                System.out.println(p);
//            }
//        });

    private static void displayPeople(List<Person> people, Predicate<Person> pred) {

        people.forEach(p -> {
            if (pred.test(p)) {
                System.out.println(p);
            }
        });
    }

}
