/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.complexity;

/**
 *
 * @author cstan
 */
public class Processor {

    /**
     *
     * @param myCar.
     */
    public void process(final Car myCar) {
        if (myCar.isNotMine()) {
            return;
        }
        myCar.paint("red");
        myCar.changeWheel();
        while (myCar.hasGazol() && myCar.getDriver().isNotStressed()) {
            myCar.drive();
        }
        return;
    }
}
