/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package overriding;

/**
 *
 * @author cstan
 */
public class Dog extends Animal {

    private String name;
    private String code;

    public Dog (String name, String code) {
        super(name);
        this.code = code;
    } 
    
    public void move() {
        System.out.println("Dogs can walk and run");
    }

    public String bark(String in) {
        System.out.println("Dogs can bark");
        return "bark";
    }
    
    public int getNumber() {
        return 1;
    }
    
}
