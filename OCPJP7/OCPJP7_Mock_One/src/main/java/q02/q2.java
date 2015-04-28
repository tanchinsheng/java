/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q02;

import java.math.BigDecimal;

/**
 *
 * @author cstan
 */
public class q2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Number[] numbers = new Number[4];
        // numbers[0] = new Number(0); //Number is abstract, cannot be instantiated.
        numbers[1] = new Integer(1);
        numbers[2] = new Float(2.0f);
        numbers[3] = new BigDecimal(3.0);

        for (Number num : numbers) {
            System.out.print(num + " ");
        }

    }

}
