package AggregatingStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class SumAndAverage {

    public static void main(String args[]) {

        List<Person> people = new ArrayList<>();

        people.add(new Person("Joe", 48));
        people.add(new Person("Mary", 30));
        people.add(new Person("Mike", 73));

        int sum = people.stream()
                .mapToInt(p -> p.getAge())
                .sum();

        System.out.println("Sum: " + sum);

        OptionalDouble avg = people.stream()
                .mapToInt(p -> p.getAge())
                .average();

        if (avg.isPresent()) {
            System.out.println("Avg: " + avg.getAsDouble());
        } else {
            System.out.println("Nothing");
        }
    }

}
