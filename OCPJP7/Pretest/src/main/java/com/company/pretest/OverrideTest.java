package com.company.pretest;


class Base {

    public static void foo(Base bObj) {
        System.out.println("In Base.foo()");
        bObj.bar();
    }

    public void bar() {
        System.out.println("In Base.bar()");
    }
}

class Derived extends Base {

    public static void foo(Base bObj) {
        System.out.println("In Derived.foo()");
        bObj.bar();
    }

    public void bar() {
        System.out.println("In Derived.bar()");
    }
}

class OverrideTest {

//    In Base.foo()
//    In Derived.bar()
// A static method is resolved statically. Inside the static method, 
// a virtual method is invoked, which is resolve dynamically.
    public static void main(String[] args) {
        Base bObj = new Derived();
        bObj.foo(bObj);
    }
}