package com.st.mapdemo;

import java.util.*;

class MapDemo {

    public static void main(String[] args) {

        //create a Map for id numbers (Integers) and names (Strings)...
        final Map<Integer, String> map = new HashMap<Integer, String>();

        //put data into Map...
        map.put(1234, "Maddin");
        map.put(5678, "Egoyan");
        map.put(9012, "Quay");

        //here's my Map (keys and values)...
        System.out.println("Original Map: " + map);

        //get a Set of Map.Entry instances, where
        //each Map.Entry has a key (Integer) and a value (String)...
        final Set<Map.Entry<Integer, String>> setOfEntries = map.entrySet();

        //here's my Set (Map.Entry instances)...
        System.out.println("Original Set: " + setOfEntries);

        //for each Map.Entry in the Set, print the id and name...
        //then change the name to "Khanjian"...
        for (Map.Entry<Integer, String> entry : setOfEntries) {
            System.out.println(" Key: " + entry.getKey());
            System.out.println(" Value: " + entry.getValue());
            entry.setValue("Khanjian");
        }

        //now here's my revised Set (with the names changed)...
        System.out.println("Revised Set: " + setOfEntries);

        //and the revised Map...
        System.out.println("Map: " + map);
    }
}