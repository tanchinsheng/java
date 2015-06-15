/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assessment;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
/*
 * /* ferret */



*/


public class Q18 {

    public static void main(String[] args) {
        System.out.println(LocalDate.of(2015, Calendar.APRIL, 1));
        System.out.println(LocalDate.of(2015, Month.APRIL, 1));
        System.out.println(LocalDate.of(2015, 3, 1));
        System.out.println(LocalDate.of(2015, 4, 1));
//        System.out.println(new LocalDate.of(2015, 3, 1));
//        System.out.println(new LocalDate.of(2015, 4, 1));
    }

}
