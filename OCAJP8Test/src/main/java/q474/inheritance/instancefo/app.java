/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q474.inheritance.instancefo;

/**
 *
 * Which of these boolean expressions correctly identifies when an object 'o'
 * actually refers to an object of class B and not of C?
 */
class A {
}

class B extends A {
}

class C extends B {
}

public class app {

    /**
     * The expression (o instanceof B) will return true if the object referred
     * to by o is of type B or a subtype of B. The expression (! (o instanceof
     * C)) will return true unless the object referred to by o is of type C or a
     * subtype of C. Thus, the expression (o instanceof B) && (!(o instanceof
     * C)) will only return true if the object is of type B or a subtype of B
     * that is not C or a subtype of C. Given objects of classes A, B and C,
     * this expression will only return true for objects of class B.
     */
    public static void main(String[] args) {
        Object o = new B();
        if ((o instanceof B) && (!(o instanceof A))) {
            System.out.println("true1");
        } //This will return false if o refers to an Object of class A, B, or C because (o instanceof A)
        // will be true for all the three.
        if (!((o instanceof A) || (o instanceof B))) {
            System.out.println("true2");
        }
        if ((o instanceof B) && (!(o instanceof C))) {
            System.out.println("true3"); //true
        }
        if (!(!(o instanceof B) || (o instanceof C))) {
            System.out.println("true4"); //true
        } //This is the complement of "(o instanceof B) && (!(o instanceof C))" prefixed with a '!'.
        // So in effect, both are same.
        if ((o instanceof B) && !((o instanceof A) || (o instanceof C))) {
            System.out.println("true5");
        }
    }
}
