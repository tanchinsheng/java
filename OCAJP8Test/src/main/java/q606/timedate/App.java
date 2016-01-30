/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q606.timedate;

import java.time.LocalDate;
import java.time.Period;

/**
 *
 * Which of the following options correct add 1 month and 1 day to a given
 * LocalDate -
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static LocalDate process(LocalDate ld) {
        //INSERT CODE HERE      
        //LocalDate ld2 = ld.plus(Period.ofMonths(1).ofDays(1));
        //LocalDate ld2 = ld.plus(new Period(0, 1, 1));
        //LocalDate ld2 = ld.plus(new Period(31)).plus(new Period(1));
        LocalDate ld2 = ld.plus(Period.of(0, 1, 1));
        return ld2;
    }

    public static void main(String[] args) {
        System.out.println(process(LocalDate.now()));
    }

}
