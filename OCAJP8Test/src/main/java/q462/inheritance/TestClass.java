/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q462.inheritance;

/**
 *
 * Which of the following statements are correct?
 */
class Super {
}

class Sub extends Super {
}

public class TestClass {

    /**
     * Note that s2 is a variable of class Sub, which is a subclass of Super. s1
     * is a variable of class Super. A subclass can ALWAYS be assigned to a
     * super class variable without any cast. It will always compile and run
     * without any exception. For example, a Dog  "IS A" Animal, so you don't
     * need to cast it. But an Animal may not always be a Dog. So you need to
     * cast it to make it compile and during the runtime the actual object
     * referenced by animal should be a Dog  otherwise it will throw a
     * ClassCastException.
     */
    public static void main(String[] args) {
        Super s1 = new Super(); //1
        Sub s2 = new Sub();     //2
        s1 = (Super) s2;        //3
    }

}
