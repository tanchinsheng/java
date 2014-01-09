/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polymorphism;

/**
 *
 * @author cstan
 */
public class Polymorphism {

    public static void main(String[] args) {
        Salary s = new Salary("Mohd Mohtashim", "Ambehta,  UP",
                3, 3600.00);
        Employee e = new Salary("John Adams", "Boston, MA",
                2, 2400.00);
        System.out.println("Call mailCheck using Salary reference --");
        s.mailCheck();
        
        System.out.println("\nCall mailCheck using Employee reference--");
        e.mailCheck();
        
        /*This behavior is referred to as virtual method invocation, 
         * and the methods are referred to as virtual methods. 
         * All methods in Java behave in this manner, 
         * whereby an overridden method is invoked at run time, 
         * no matter what data type the reference is that was used in the source code at compile time.*/
    }
}
