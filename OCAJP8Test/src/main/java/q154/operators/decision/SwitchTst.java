/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q154.operators.decision;

/**
 *
 * @author cstan
 */
public class SwitchTst {

    /**
     * You cannot have unlabeled block of code inside a switch block. Any code
     * block must succeed a case label (or default label). Since there is no
     * case statement in this switch block, there is no way the line flag =
     * true; can be reached! Therefore, it will not compile.
     */
    public static void main(String args[]) {
        for (int i = 0; i < 3; i++) {
            boolean flag = false;
        }
        switch (i) { // It will say 'case', 'default' or '}' expected at compile time.


        flag= true;
     }
     if (flag) {
            System.out.println(i);
        }
    }

}
