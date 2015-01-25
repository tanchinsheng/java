package com.st.testwildcards;

import java.util.*;

public class TestWildcards {

    public static void main(String[] args) {
        List<Integer> myList = new ArrayList<Integer>();
        Bar bar = new Bar();
        bar.doInsert(myList);
    }
}

class Dog{}

class Bar {

    //void doInsert(List<?> list) {
    void doInsert(List<? extends Object> list) {
        list.add(new Dog());
        
    }
}
