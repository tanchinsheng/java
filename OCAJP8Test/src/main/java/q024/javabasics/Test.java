/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q024.javabasics;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    int i1;
    static int i2;

    public void method1() {
        int i;
        // ... insert statements here  
        i = this.i1; // ok , As i1 is an instance variable, it is accessible through 'this'.
        i = this.i2; //ok, You cannot do this.i as i is a local variable.
        // this = new Test(); //nok, Nope, you can't change this.
        //this.i = 4; //  nok, You cannot do this.i as i is a local variable.
        this.i1 = i2; //ok, You are just assigning a static field's value to non-static field.
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
