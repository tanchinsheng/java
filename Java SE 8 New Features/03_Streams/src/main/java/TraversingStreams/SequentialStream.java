package TraversingStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SequentialStream {

    public static void main(String args[]) {

        List<Person> people = new ArrayList<>();

        people.add(new Person("Joe", 48));
        people.add(new Person("Mary", 30));
        people.add(new Person("Mike", 73));

        Predicate<Person> pred = (p) -> p.getAge() > 65;

        displayPeople(people, pred);

    }

    private static void displayPeople(List<Person> people, Predicate<Person> pred) {
        System.out.println("Selected:");
//        people.forEach(p -> {
//            if (pred.test(p)) {
//                System.out.println(p.getName());
//            }
//        });

        people.stream().filter(pred).forEach(p -> System.out.println(p.getName()));

    }

}
