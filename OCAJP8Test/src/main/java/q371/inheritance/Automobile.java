/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q371.inheritance;

/**
 *
 * Here are some points to remember: A class is uninstantiable if the class is
 * declared abstract. If a method has been declared as abstract, it cannot
 * provide an implementation (i.e. it cannot have a method body ) and the class
 * containing that method must be declared abstract). If a method is not
 * declared abstract, it must provide a method body (the class can be abstract
 * but not necessarily so). If any method in a class is declared abstract, then
 * the whole class must be declared abstract. An class can still be made
 * abstract even if it has no abstract method.
 */
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
