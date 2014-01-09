/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstraction;

/**
 *
 * @author cstan
 */
public class Abstraction {

    public static void main(String[] args) {
        Salary s = new Salary("Mohd Mohtashim", "Ambehta,  UP",
                3, 3600.00);
        Employee e = new Salary("John Adams", "Boston, MA",
                2, 2400.00);

        System.out.println("Call mailCheck using Salary reference --");
        s.mailCheck();
        System.out.println("\nCall mailCheck using Employee reference --");
        e.mailCheck();
    }
}
