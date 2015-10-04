/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q427.inheritance;

/**
 *
 * Which of these statements are legal?
 */
interface I1 {
}

interface I2 {
}

class C1 implements I1 {
}

class C2 implements I2 {
}

class C3 extends C1 implements I2 {
}

class C4 extends C3 implements I1, I2 {//legal
}//Although, the implements I1, I2 is redundant here because C3 already implements I1 and I2, it is not invalid.

public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        C1 o1;
        C2 o2;
        C3 o3;
        o3 = o1;// superclass reference cannot be assigned to subclass reference without explicit cast.
        o3 = o2; //There is no way a reference of class C2 (which is o2) can point to an object of class C3
        // because C2 and C3 have no inheritance relationship.
        // So this assignment is rejected at compile time itself.
        I1 i1 = o3; //legal
        I2 i2 = (I2) i1; //legal
        //This is valid because at run time i1 actually refers to an object that implements I2.
        I1 b = o3; //legal
        //Because C3 extends C1 which implements I1.
    }

}
