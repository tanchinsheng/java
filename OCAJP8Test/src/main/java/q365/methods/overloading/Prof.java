/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q365.methods.overloading;

/**
 *
 * Which of the following methods can be inserted at line //1 ?
 */
class Teacher {

    void teach(String student) {
        /* lots of code */
    }
}

class Prof extends Teacher {

    /**
     * Note that 'protected' is less restrictive than default 'no modifier'. So
     * choice 3 is valid. "public abstract void teach(String s)" would have been
     * valid if class Prof had been declared abstract.
     */
    //1
    public void teach()
            throws Exception {
    } // It overloads the teach() method instead of overriding it.

    private void teach(int i)
            throws Exception {
    } // It overloads the teach() method instead of overriding it.

    @Override
    protected void teach(String s) {
    } // This overrides Teacher's teach method. The overriding method can have more visibility.
    // (It cannot have less. Here, it cannot be private.)

    public final void teach(String s) {
    } // Overriding method may be made final.

    public abstract void teach(String s);
    // This is wrong because class Prof has not been declared as abstract.
    // Note that otherwise it is OK to override a method by an abstract method.

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
