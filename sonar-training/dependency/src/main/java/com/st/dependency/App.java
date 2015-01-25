package com.st.dependency;

import B.ClassB;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        
        ClassB classb = new ClassB();
        classb.setB("aa");
        System.out.println("Hello World!");
    }
}
