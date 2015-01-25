package com.st.maptest;

import java.util.*;

class Dog {

    public Dog(String n) {
        name = n;
    }
    public String name;

    @Override
    public boolean equals(Object o) {

        //if ((o instanceof Dog) && (((Dog) o).name == name)) {
        if ((o instanceof Dog) && (((Dog) o).name == null ? name == null : ((Dog) o).name.equals(name))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.length();
    }
}

class Cat {
}

enum Pets {

    DOG, CAT, HORSE
}

class MapTest {

    public static void main(String[] args) {
        Map<Object, Object> m = new HashMap<Object, Object>();
        m.put("k1", new Dog("aiko")); // add some key/value pairs
        m.put("k2", Pets.DOG);
        m.put(Pets.CAT, "CAT key");
        Dog d1 = new Dog("clover"); // let's keep this reference
        m.put(d1, "Dog key");
        m.put(new Cat(), "Cat key");
        System.out.println(m.get("k1")); // #1
        String k2 = "k2";
        System.out.println(m.get(k2)); // #2
        Pets p = Pets.CAT;
        System.out.println(m.get(p)); // #3
        System.out.println(m.get(d1)); // #4
        System.out.println(m.get(new Cat())); // #5
        System.out.println(m.size()); // #6
//The Dog that was previously found now cannot be found. Because the Dog.name
//variable is used to create the hashcode, changing the name changed the value of the
//hashcode.
        d1.name = "magnolia";
        System.out.println(m.get(d1));

        d1.name = "clover";
        System.out.println(m.get(new Dog("clover"))); // #2
        d1.name = "arthur";
        System.out.println(m.get(new Dog("clover"))); // #3
        
//        In the first call to get(), the hashcode is 8 (magnolia) and it should be 6
//(clover), so the retrieval fails at step 1 and we get null. In the second call to
//get(), the hashcodes are both 6, so step 1 succeeds. Once in the correct bucket (the
//"length of name = 6" bucket), the equals() method is invoked, and since Dog's
//equals() method compares names, equals() succeeds, and the output is Dog key.
//In the third invocation of get(), the hashcode test succeeds, but the equals() test
//fails because arthur is NOT equal to clover.
        

    }
}
