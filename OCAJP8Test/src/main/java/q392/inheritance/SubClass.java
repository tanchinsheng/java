/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q392.inheritance;

/**
 *
 * Which of the following constructors MUST exist in SuperClass for SubClass to
 * compile correctly?
 */
class SuperClass {

    public SuperClass() {
    } // The default no args constructor will not be provided because SuperClass has to define one arg constructor.

    public SuperClass(int a) {
    } // Because it is called in the second constructor of SubClass.
}

public class SubClass extends SuperClass {

    /**
     * @param args the command line arguments
     */
    int i, j, k;

    public SubClass(int m, int n) {
        i = m;
        j = m;
    } //1

    public SubClass(int m) {
        super(m);
    } //2

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
