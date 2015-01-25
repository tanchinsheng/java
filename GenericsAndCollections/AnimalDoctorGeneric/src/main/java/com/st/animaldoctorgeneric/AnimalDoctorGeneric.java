package com.st.animaldoctorgeneric;

import java.util.*;

abstract class Animal {

    public abstract void checkup();
}

class Dog extends Animal {

    public void checkup() { // implement Dog-specific code
        System.out.println("Dog checkup");
    }
}

class Cat extends Animal {

    public void checkup() { // implement Cat-specific code
        System.out.println("Cat checkup");
    }
}

class Bird extends Animal {

    public void checkup() { // implement Bird-specific code
        System.out.println("Bird checkup");
    }
}

public class AnimalDoctorGeneric {
    // change the argument from Animal[] to ArrayList<Animal>

    public void checkAnimals(ArrayList<Animal> animals) {
        for (Animal a : animals) {
            a.checkup();
        }
    }

    public void addAnimal(List<Animal> animals) {
        animals.add(new Dog()); // this is always legal,
// since Dog can
// be assigned to an Animal
// reference
    }

    public static void main(String[] args) {
// make ArrayLists instead of arrays for Dog, Cat, Bird
        List<Dog> dogs = new ArrayList<Dog>();
        dogs.add(new Dog());
        dogs.add(new Dog());
        List<Cat> cats = new ArrayList<Cat>();
        cats.add(new Cat());
        cats.add(new Cat());
        List<Bird> birds = new ArrayList<Bird>();
        birds.add(new Bird());
// this code is the same as the Array version
       // AnimalDoctorGeneric doc = new AnimalDoctorGeneric();
// this worked when we used arrays instead of ArrayLists
//        doc.checkAnimals(dogs); // send a List<Dog>
//        doc.checkAnimals(cats); // send a List<Cat>
//        doc.checkAnimals(birds); // send a List<Bird>
//        javac AnimalDoctorGeneric.java
//AnimalDoctorGeneric.java:51: checkAnimals(java.util.
//ArrayList<Animal>) in AnimalDoctorGeneric cannot be applied to
//(java.util.List<Dog>)
//doc.checkAnimals(dogs);
//^
//AnimalDoctorGeneric.java:52: checkAnimals(java.util.
//ArrayList<Animal>) in AnimalDoctorGeneric cannot be applied to
//(java.util.List<Cat>)
//doc.checkAnimals(cats);
//^
//AnimalDoctorGeneric.java:53: checkAnimals(java.util.
//ArrayList<Animal>) in AnimalDoctorGeneric cannot be applied to
//(java.util.List<Bird>)
//doc.checkAnimals(birds);
//^
//3 errors
        Animal[] animals1 = new Animal[3];
        animals1[0] = new Cat();
        animals1[1] = new Dog();

        List<Animal> animals2 = new ArrayList<Animal>();
        animals2.add(new Cat()); // OK
        animals2.add(new Dog()); // OK 

        List<Animal> animals = new ArrayList<Animal>();
        animals.add(new Dog());
        animals.add(new Dog());
        AnimalDoctorGeneric doc = new AnimalDoctorGeneric();
        doc.addAnimal(animals); // OK, since animals matches
// the method arg

    }
}