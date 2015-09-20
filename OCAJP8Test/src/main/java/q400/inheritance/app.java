/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q400.inheritance;

/**
 *
 * Which of the following are valid declarations inside an interface independent
 * of each other?
 */
public interface app {

    /**
     * @param args the command line arguments
     */
    void compute1();  // All interface methods have to be public.
    // No access control keyword in the method declaration also means public in an interface.
    // (Note that the absence of access control keyword in the method declaration in a class means package protected.)

    public void compute2();

    public final void compute3(); // final is not allowed.

    static void compute4(); // An interface can have a static method but the method must have a body in that case.

    protected void compute5(); //All interface methods have to be public

}
