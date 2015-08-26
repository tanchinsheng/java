/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q084.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * The concept to understand here is as follows - If the compiler can figure
     * out that something can NEVER happen, then it flags an error. In this
     * question, the compiler knows that ln, i or d can never point to the same
     * object in any case because they are references to different classes of
     * objects that have no relation ( superclass/subclass ) between themselves.
     */
    public static void main(String[] args) {
        Integer i = new Integer(42);
        Long ln = new Long(42);
        Double d = new Double(42.0);

        if (i == ln) {
        };
        if (ln == d) {
        };
        if (i.equals(d)) {
        };
        if (d.equals(ln)) {
        };
        //Due to auto-boxing int 42 is converted into an Integer object containing 42.
        // So this is valid. It will return false though because ln is a Long and 42 is boxed into an Integer.
        if (ln.equals(43)) {
        };

    }

}
