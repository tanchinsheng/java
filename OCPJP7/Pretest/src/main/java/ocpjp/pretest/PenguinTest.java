package ocpjp.pretest;


class CannotFlyException extends Exception {
}

interface Birdie {

    public abstract void fly() throws CannotFlyException;
}

interface _9Biped {

    public void walk();
}

abstract class _9_NonFlyer {

    public void fly() {
        System.out.print("cannot fly ");
    } // LINE A
}

class Penguin extends _9_NonFlyer implements Birdie, _9Biped { // LINE B
    public void walk() {
        System.out.print("walk ");
    }
}

class PenguinTest {

    //When executed, the program prints “walk cannot fly”.
    //In order to override a method, it is not necessary for the overridden method
    // to specify an exception. However, if the exception is specified, 
    // then the specified exception must be the same or a subclass of the specified exception
    //in the method defined in the super class (or interface).
    public static void main(String[] args) {
        Penguin pingu = new Penguin();
        pingu.walk();
        pingu.fly();
    }
}