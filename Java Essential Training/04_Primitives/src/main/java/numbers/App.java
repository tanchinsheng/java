/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package numbers;

import java.math.BigDecimal;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BigDecimal payment = new BigDecimal(1115.37);
        System.out.println(payment.toString());  // 1115.3699999999998908606357872486114501953125

        double d = 1115.37;
        String ds = Double.toString(d);
        BigDecimal bd = new BigDecimal(ds);
        System.out.println(bd.toString());

    }

}
