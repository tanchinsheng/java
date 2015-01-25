/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.complexity;

/**
 *
 * @author cstan
 */
public class Car {

    /**
     * my color.
     */
    private String color;
    /**
     * my driver.
     */
    private Driver driver;

    /**
     * Get the value of driver.
     * Is this a public API?
     * @return the value of driver
     */
    public Driver getDriver() {

        return driver;
    }

    public void paint(String color) {
        this.color = color;
    }

    public void drive() {
    }
    
    public Boolean isNotMine() {
        return true;
    }

    public void changeWheel() {
    }

    public Boolean hasGazol() {
        return true;
    }
}
