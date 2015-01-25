/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package B;

import A.ClassA;

/**
 *
 * @author cstan
 */
public class ClassB {

    private String B;

    /**
     * Get the value of B
     *
     * @return the value of B
     */
    public String getB() {

        ClassA classA = new ClassA();
        return classA.getEatA();

        //return B;
    }

    /**
     * Set the value of B
     *
     * @param B new value of B
     */
    public void setB(String B) {
        this.B = B;

        ClassA classA = new ClassA();
        classA.setEatA("xx");

    }
}
