/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q318.methods;

/**
 *
 * What should be the return type of the following method?
 */
public class app {

    /**
     * Note that the cast (long) applies to 'by' not to the whole expression. (
     * (long) by ) / d * 3; Now, division operation on long gives you a double.
     * So the return type should be double
     */
    public RETURNTYPE methodX(byte by) { // Correct=double
        double d = 10.0;
        return (long) by / d * 3;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
