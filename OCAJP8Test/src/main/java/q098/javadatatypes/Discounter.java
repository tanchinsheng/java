/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q098.javadatatypes;

/**
 *
 * @author cstan
 */
public class Discounter {

    /**
     * Remember that static and instance variables are automatically assigned a
     * value even if you don't initialize them explicitly but local variables
     * must be initialized explicitly before they are used. Now, observe that
     * the calc method declares local variables coupon, offset, and base but
     * does not assign them a value. Even though at run time, we know that since
     * percent is 0 and is thus < 10, a value will be assigned to these
     * variables, the compiler doesn't know this because the compiler doesn't
     * take values of "variables" into consideration while determining the flow
     * of control. It only considers the values of compile time constants.
     * Therefore, as far as the compiler is concerned, coupon, offset, and base
     * may remain uninitialized before they are used. Having uninitialized
     * variables itself is not a problem. So there is no compilation error at
     * //3. However, using them before they are initialized is a problem and
     * therefore the compiler flags an error at //5. Had percent been defined as
     * final ( static final double percent = 0; ), the code would work and print
     * 3000.0.
     */
    static double percent; //1     
    int offset = 10, base = 50; //2    

    public static double calc(double value) {
        int coupon, offset, base; //3

        if (percent
                < 10) {//4

            coupon = 15;

            offset = 20;

            base = 10;
        }

        return coupon * offset * base * value / 100; //5     
    }

    public static void main(String[] args) {
        System.out.println(calc(100));
    }

}
