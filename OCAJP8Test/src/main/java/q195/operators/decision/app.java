/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q195.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static boolean getBool() {
        return true;
    }

    public static String getString() {
        return "true";
    }

    public static void main(String args[]) {
        //A boolean cannot be used in a case statement. So, as it is, the given code will not compile.
        //switch (getBool()) {
        switch (getString()) {
            //case true:
            case "true":
                System.out.println("true");
                break;
            default:
                System.out.println("none");
                break;
        }
    }
}
