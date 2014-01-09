package com.company.pretest;

import java.lang.*;

class InvalidValueException extends IllegalArgumentException {
}

class InvalidKeyException extends IllegalArgumentException {
}

class BaseClass {

    void foo() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
}

class DeriClass extends BaseClass {

    public void foo() throws IllegalArgumentException {
        throw new InvalidValueException();
    }
}

class DeriDeriClass extends DeriClass {

    public void foo() { // LINE A
        throw new InvalidKeyException();
    }
}

class EHTest {

    public static void main(String[] args) {
        try {
            BaseClass base = new DeriDeriClass();
            base.foo();
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}