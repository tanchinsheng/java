/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q120.javadatatypes.gc;

/**
 *
 * Which of the following statements are correct?
 */
public class N {

    /**
     * There are no instances of M to be garbage collected till the program
     * ends.
     */
    private M m = new M();

    public void makeItNull(M pM) {
        pM = null;
    }

    public void makeThisNull() {
        makeItNull(m);
    }

    public static void main(String[] args) {
        N n = new N();
        n.makeThisNull();
    }

}
