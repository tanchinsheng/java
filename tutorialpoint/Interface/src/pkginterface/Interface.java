/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkginterface;

/**
 *
 * @author cstan
 */
public class Interface {

    /* A class describes the attributes and behaviors of an object. 
     * An interface contains behaviors that a class implements.*/
    /*
     You cannot instantiate an interface.

     An interface does not contain any constructors.

     All of the methods in an interface are abstract.

     An interface cannot contain instance fields. 
     * The only fields that can appear in an interface must be declared both static and final.

     An interface is not extended by a class; it is implemented by a class.

     An interface can extend multiple interfaces.
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        MammalInt m = new MammalInt();
        m.eat();
        m.travel();
    }
}
