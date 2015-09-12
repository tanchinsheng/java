/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q179.operators.decision;

/**
 *
 * @author cstan
 */
public class Switcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        switch (Integer.parseInt(args[1])) //1
        {
            case 0:
                boolean b = false;
                break;

            case 1:
                b = true; //2
                break;
        }

        if (b) {
            System.out.println(args[2]);
        }
        // It will not compile because of if(b) because b is declared in the switch block and it is out of scope
        // after the switch block ends. Pay close attention to question text. It may seem to test you on one
        // concept but actually it could be testing something entirely different.
    }

}
