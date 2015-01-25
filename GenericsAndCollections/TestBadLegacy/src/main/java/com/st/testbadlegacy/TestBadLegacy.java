package com.st.testbadlegacy;

import java.util.*;

public class TestBadLegacy {

    public static void main(String[] args) {
        List<Integer> myList = new ArrayList<Integer>();
        myList.add(4);
        myList.add(6);
        Inserter in = new Inserter();
        in.insert(myList); // pass List<Integer> to legacy code
        int x = myList.get(0); // you must cast !!
    }
}

class Inserter {
// method with a non-generic List argument
    void insert(List list) {
        list.add(new Integer(42)); // adds to the incoming list
//        Will that work? Yes, sadly, it does! It both compiles and runs. No runtime
//exception. Yet, someone just stuffed a String into a supposedly type safe ArrayList of
//type <Integer>.
//        How can that be?
//Remember, the older legacy code was allowed to put anything at all (except
//primitives) into a collection.
        list.add(new String("42"));
    }
    
//    C:\git\JavaEE\GenericsAndCollections\TestBadLegacy\src\main\java\com\st\testbadlegacy>C:\Java\x64\jdk1.6.0_33\bin\javac -Xlin
//t:unchecked TestBadLegacy.java
//TestBadLegacy.java:19: warning: [unchecked] unchecked call to add(E) as a member of the raw type java.util.List
//        list.add(new Integer(42)); // adds to the incoming list
//                ^
//TestBadLegacy.java:26: warning: [unchecked] unchecked call to add(E) as a member of the raw type java.util.List
//        list.add(new String("42"));
//                ^
//2 warnings
    
}