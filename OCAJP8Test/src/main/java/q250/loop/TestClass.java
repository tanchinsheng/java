/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q250.loop;

/**
 *
 * What is the effect of compiling and running the code shown in exhibit?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        int sum = 0;
        for (int i = 0, j = 10; sum > 20; ++i, --j) // 1
        // Note that the loop condition is sum >20 and not sum <20.
        {
            sum = sum + i + j;
        }
        System.out.println("Sum = " + sum); // 0
    }

}
