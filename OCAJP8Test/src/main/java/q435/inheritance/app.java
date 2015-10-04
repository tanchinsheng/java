/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q435.inheritance;

/**
 *
 * Which of the following options can be inserted in PremiumAccount independent
 * of each other?
 */
interface Account {

    public default String getId() {
        return "0000";
    }
}

interface PremiumAccount extends Account {

    //INSERT CODE HERE }
//    static String getId() {//nok
//        return "1111";
//    } // You cannot override a static method with a non-static method and vice-versa.
    // String getId();//ok // An interface can redeclare a default method and also make it abstract.
//    default String getId() { //ok
//        return "1111";
//    } // An interface can redeclare a default method and provide a different implementation.
    //abstract static String getName(); //nok
    // 1. static methods can never be abstract (neither in an interface not in a class).
    // 2. An interface can have a static method but the method must have a body.
    //static String getName();//nok: An interface can have a static method but the method must have a body.
    //default String getName(); //nok :A default method must have a body.
}

public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
