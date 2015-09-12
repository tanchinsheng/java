/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q149.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    // The if statement does not return any value so it can not be used the way it is used in (1).
    int max1(int x, int y) {
        return (
        if (x > y) {x;
        } else {y;
        });
    }

    // It would work if the first return and the corresponding brackets is removed.
    int max2(int x, int y) {
        return ( if (x > y) {
            return x;
        } else {
            return y;
        });

    }

    // Neither the switch expression nor the case labels can be of type boolean.
    int max3(int x, int y) {
        switch (x < y) {
            case true:
                return y;
            default:
                return x;
        };
    }

    int max4(int x, int y) {
        if (x > y) {
            return x;
        }
        return y;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
