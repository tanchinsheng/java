/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q181.operators.decision;

/**
 *
 * Which of the lines will cause a compile time error in the following program?
 */
public class MyClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        char c;
        int i;
        c = 'a';//1
        i = c;  //2
        i++;    //3
        c = i;  //4
        c++;    //5
    }

}
