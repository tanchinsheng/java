/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q389.inheritance;

/**
 *
 * Which of the following methods can be inserted in class Y?
 */
class A {
}

class B extends A {
}

class C extends B {
}

class X {

    B getB() {
        return new B();
    }
}

class Y extends X {

    /**
     * @param args the command line arguments
     */
    //method declaration here
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
