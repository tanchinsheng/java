/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package A;

import B.ClassB;

/**
 *
 * @author cstan
 */
public class ClassA {
   
    private String eatA;

    /**
     * Get the value of eat
     *
     * @return the value of eat
     */
    public String getEatA() {
        ClassB classB = new ClassB();        
        return classB.getB();
    }

    /**
     * Set the value of eat
     *
     * @param eat new value of eat
     */
    public void setEatA(String eatA) {
        ClassB classB = new ClassB(); 
        classB.setB("xxx");
        this.eatA = eatA;
    }

    
}
