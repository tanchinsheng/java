/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q273.loop;

/**
 *
 * In the following code what will be the output if 0 (integer value zero) is
 * passed to loopTest()?
 */
public class TestClass {

    /**
     * When x is 0, the statement continue loop; is executed. Note that loop: is
     * for the outer loop. So, only one iteration (that too not full) is
     * performed for the inner loop. So, the inner loop prints the value of i
     * only once and then next iteration of outer loop starts. 'j' is never
     * printed. So, it prints 1 2 3 4.
     */
    public void loopTest(int x) {
        loop:
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                System.out.println(i);
                if (x == 0) {
                    continue loop;
                }
                System.out.println(j);
            }
        }
    }

    public static void main(String[] args) {
        new TestClass().loopTest(0);
    }

}
