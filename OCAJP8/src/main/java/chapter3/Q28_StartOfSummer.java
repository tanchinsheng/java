/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;

/**
 *
 * @author cstan
 */
public class Q28_StartOfSummer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LocalDate date1 = LocalDate.of(2014, 5, 21);
        System.out.println(date1);
        LocalDate date2 = LocalDate.of(2014, 6, 21);
        System.out.println(date2);
        LocalDate date3 = LocalDate.of(2014, Calendar.JUNE, 21);
        System.out.println(date3);
        LocalDate date4 = LocalDate.of(2014, Month.JUNE, 21);
        System.out.println(date4);
    }

}
