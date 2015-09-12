/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q286.loop;

/**
 *
 * Which of the following statements are true?
 */
public class Test {

    /**
     * The scope of a local variable declared in 'for' statement is the rest of
     * the 'for' statement, including its own initializer. So, when line 3 is
     * placed before line 1, there is a redeclaration of i in the first for()
     * which is not legal. As such, the scope of i's declared in for() is just
     * within the 'for' blocks. So placing line 4 before line 3 will not work
     * since 'i' is not in scope there.
     */
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");  //1
        }
        for (int i = 10; i > 0; i--) {
            System.out.print(i + " ");  //2
        }
        int i = 20;                  //3
        System.out.print(i + " ");   //4
    }
}
