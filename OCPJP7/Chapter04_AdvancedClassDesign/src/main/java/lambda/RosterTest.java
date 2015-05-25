/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambda;

/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class RosterTest {

    // functioanl interface,  contains only one abstract method.
    interface CheckPerson {

        // no implementation, only declaration
        boolean test(Person p);
    }

    // Approach 1: Create Methods that Search for Persons that Match One
    // Characteristic
    public static void printPersonsOlderThan(List<Person> roster, int age) {
        for (Person p : roster) {
            if (p.getAge() >= age) {
                p.printPerson();
            }
        }
    }

    // Approach 2: Create More Generalized Search Methods
    public static void printPersonsWithinAgeRange(List<Person> roster, int low, int high) {
        for (Person p : roster) {
            if (low <= p.getAge() && p.getAge() < high) {
                p.printPerson();
            }
        }
    }

    // Approach 3: Specify Search Criteria Code in a Local Class
    // Approach 4: Specify Search Criteria Code in an Anonymous Class
    // Approach 5: Specify Search Criteria Code with a Lambda Expression
    public static void printPersons(List<Person> roster, CheckPerson tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }

    // Approach 6: Use Standard Functional Interfaces with Lambda Expressions
    public static void printPersonsWithPredicate(List<Person> roster, Predicate<Person> tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }

    // The Consumer<T> interface contains the method void accept(T t), which has these characteristics.
    // The following method replaces the invocation p.printPerson() with an instance of Consumer<Person>
    // that invokes the method accept:
    // Approach 7: Use Lambda Expressions Throughout Your Application
    public static void processPersons(List<Person> roster, Predicate<Person> tester, Consumer<Person> block) {
        for (Person p : roster) {
            if (tester.test(p)) {
                block.accept(p);
            }
        }
    }

    // What if you want to do more with your members' profiles than printing them out.
    // Suppose that you want to validate the members' profiles or retrieve their contact information?
    // In this case, you need a functional interface that contains an abstract method that returns a value.
    // The Function<T,R> interface contains the method R apply(T t).
    // The following method retrieves the data specified by the parameter mapper,
    // and then performs an action on it specified by the parameter block:
    // Approach 7, second example
    public static void processPersonsWithFunction(
            List<Person> roster,
            Predicate<Person> tester,
            // Interface Function<T,R>
            // T - the type of the input to the function
            // R - the type of the result of the function
            Function<Person, String> mapper,
            Consumer<String> block) {
        for (Person p : roster) {
            if (tester.test(p)) {
                String data = mapper.apply(p);
                block.accept(data);
            }
        }
    }

    // Approach 8: Use Generics More Extensively
    public static <X, Y> void processElements(
            Iterable<X> source,
            Predicate<X> tester,
            Function<X, Y> mapper,
            Consumer<Y> block) {
        for (X p : source) {
            if (tester.test(p)) {
                Y data = mapper.apply(p);
                block.accept(data);
            }
        }
    }

    public static void main(String... args) {

        List<Person> roster = Person.createRoster();

        for (Person p : roster) {
            p.printPerson();
        }

        // Approach 1: Create Methods that Search for Persons that Match One
        // Characteristic
        System.out.println("Approach 1: Persons older than 20:");
        printPersonsOlderThan(roster, 20);
        System.out.println();

        // Approach 2: Create More Generalized Search Methods
        System.out.println("Approach 2: Persons between the ages of 14 and 30:");
        printPersonsWithinAgeRange(roster, 14, 30);
        System.out.println();

        // Approach 3: Specify Search Criteria Code in a Local Class
        System.out.println("Approach 3: Persons who are eligible for Selective Service:");

        class CheckPersonEligibleForSelectiveService implements CheckPerson {

            @Override
            public boolean test(Person p) {
                return p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25;
            }
        }

        //Because CheckPersonEligibleForSelectiveService implements an interface,
        //you can use an anonymous class instead of a local class and
        //bypass the need to declare a new class for each search.
        printPersons(roster, new CheckPersonEligibleForSelectiveService());

        System.out.println();

        // Approach 4: Specify Search Criteria Code in an Anonymous Class
        System.out.println("Approach 4: Persons who are eligible for Selective Service (anonymous class):");

        // This approach reduces the amount of code required
        // because you don't have to create a new class for each search
        // that you want to perform.
        // However, the syntax of anonymous classes is bulky considering that
        // the CheckPerson interface contains only one method.
        // In this case, you can use a lambda expression instead of an anonymous class,
        // as described in the next section.
        printPersons(
                roster,
                new CheckPerson() {
                    @Override
                    public boolean test(Person p) {
                        return p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25;
                    }
                }
        );

        System.out.println();

        // Approach 5: Specify Search Criteria Code with a Lambda Expression
        System.out.println("Approach 5: Persons who are eligible for Selective Service (lambda expression):");

        printPersons(
                roster,
                // Because a functional interface contains only one abstract method,
                // you can omit the name of that method when you implement it.
                (Person p) -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25
        );

        System.out.println();

        // Approach 6: Use Standard Functional Interfaces with Lambda
        // Expressions
        System.out.println("Approach 6: Persons who are eligible for Selective Service (with Predicate parameter):");

        printPersonsWithPredicate(
                roster,
                p -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25
        );

        System.out.println();

        // Approach 7: Use Lamba Expressions Throughout Your Application
        System.out.println("Approach 7.: Persons who are eligible for Selective Service (with Predicate and Consumer parameters):");

        processPersons(
                roster,
                p -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25,
                // The Consumer<T> interface contains the method void accept(T t),
                //which has these characteristics.
                //The following method replaces the invocation p.printPerson()
                // with an instance of Consumer<Person> that invokes the method accept:
                p -> p.printPerson()
        );

        System.out.println();

        // Approach 7, second example
        System.out.println("Approach 7.2: Persons who are eligible for Selective Service "
                + "(with Predicate, Function, and Consumer parameters):");

        processPersonsWithFunction(
                roster,
                p -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25,
                p -> p.getEmailAddress(),
                email -> System.out.println(email)
        );

        System.out.println();

        // Approach 8: Use Generics More Extensively
        System.out.println("Approach 8: Persons who are eligible for Selective Service (generic version):");

        processElements(
                roster,
                p -> p.getGender() == Person.Sex.MALE
                && p.getAge() >= 18
                && p.getAge() <= 25,
                p -> p.getEmailAddress(),
                email -> System.out.println(email)
        );

        System.out.println();

        // Approach 9: Use Bulk Data Operations That Accept Lambda Expressions
        // as Parameters
        System.out.println("Approach 9: Persons who are eligible for Selective Service (with bulk data operations):");

        roster
                .stream()
                .filter(
                        p -> p.getGender() == Person.Sex.MALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25)
                .map(p -> p.getEmailAddress())
                .forEach(email -> System.out.println(email));
    }
}
