/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q171.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * Observe that (rightTurn && !pedestrian || otherLane) is true, therefore (
     * ? && !pedestrian && greenLight ) does not matter. || and && are short
     * circuit operators. So, if the first part of the expression ( i.e. part
     * before || ) is true ( or false for && ) the other part is not evaluated
     * at all. Note that this is not true for | and &. In that case, the whole
     * expression will be evaluated even if the value of the expression can be
     * known by just evaluating first part.
     */
    public static void main(String[] args) {
        boolean greenLight = true;
        boolean pedestrian = false;
        boolean rightTurn = true;
        boolean otherLane = false;
        if (((rightTurn && !pedestrian || otherLane) || ( ?  && !pedestrian && greenLight)) == true) {
            System.out.println("true");
        }
        // since the part before second || is true, the next part is not even evaluated.
    }

}
