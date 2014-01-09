/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package overriding;

/**
 *
 * @author cstan
 */
public class Animal {

    private String name;

    public Animal(String name) {
        this.name = name;
    }
    
    public void move() {
        System.out.println("Animals can move");
    }
}
