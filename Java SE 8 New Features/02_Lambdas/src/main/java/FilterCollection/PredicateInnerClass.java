package FilterCollection;

import FilterCollection.model.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PredicateInnerClass {

    public static void main(String args[]) {

        List<Person> people = new ArrayList<>();

        people.add(new Person("Joe", 48));
        people.add(new Person("Mary", 30));
        people.add(new Person("Mike", 73));

        Predicate<Person> pred = new Predicate<Person>() {
            @Override
            public boolean test(Person p) {
                return (p.getAge() >= 65);
            }
        };
        for (Person person : people) {
            if (pred.test(person)) {
                System.out.println(person);
            }
        }
//        Predicate<Person> pred = (Person p) -> (p.getAge() >= 65);
//        people.stream().filter((person) -> (pred.test(person))).forEach((person) -> {
//            System.out.println(person);
//        });
    }
}
