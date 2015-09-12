/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q257.loop;

/**
 *
 * Which of these statements are valid when occurring by themselves in a method?
 */
public class app {

    /**
     * It is not possible to break out of an if statement. But if the if
     * statement is placed within a switch statement or a loop construct, the
     * usage of break in option 3 would be valid.
     */
    void A() {
        while () { //The condition expression in a while header is required.
            break;
        }
    }

    void B() {
        do {
            break;
        } while (true);
    }

    void C() {
        if (true) {
            break;
        }
    }
    // You cannot have break or continue in an 'if' or 'else' block without being inside a loop.
    // Note that the problem statement mentions, "...occuring by themselves".
    // This implies that the given statement is not wrapped within any other block.

    void D() {
        switch (1) { //You can use a constant in switch(...);
            default:
                break;
        }
    }

    void E() {
        for (; true;) {
            break;
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
