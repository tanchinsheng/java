package q371.inheritance;

abstract class Automobile2 {

    void honk();   //(2):It will not compile as the method doesn't have the body and also is not declared abstract.
}

abstract class Automobile3 {

    void honk() {
    }
;   //(3): This is a valid abstract class although it doesn't have any abstract method.

}

abstract class Automobile4 {

    abstract void honk() {
    }   //(4): An abstract method cannot have a method body. {} constitutes a valid method body.
}

abstract class Automobile5 {

    abstract void honk();   //(5): This is a valid abstract class
}

class Automobile {

    /**
     * @param args the command line arguments
     */
    abstract void honk();  //(1):It will not compile as one of its method is abstract but the class itself is not abstract.

}
