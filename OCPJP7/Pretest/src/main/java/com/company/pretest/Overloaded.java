package com.company.pretest;

class Overloaded {

    public static void foo(Integer i) {
        System.out.println("foo(Integer)");
    }

    public static void foo(short i) {
        System.out.println("foo(short)");
    }

    public static void foo(long i) {
        System.out.println("foo(long)");
    }

    public static void foo(int... i) {
        System.out.println("foo(int ...)");
    }

//    For an integer literal, the JVM matches in the following order: int, long, Integer, int.... In other words, it
//first looks for an int type parameter; if it is not provided, then it looks for long type; and so on. Here, since the int
//type parameter is not specified with any overloaded method, it matches with foo(long).
    
    public static void main(String[] args) {
        foo(10);
    }
}