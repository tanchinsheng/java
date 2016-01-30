/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q604.timedate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * Assuming that the current date on the system is 5th Feb, 2015, which of the
 * following will be a part of the output?
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        LocalDate d1 = LocalDate.parse("2015-02-05", DateTimeFormatter.ISO_DATE);//T17:13:50");         
        LocalDate d2 = LocalDate.of(2015, 2, 5);
        LocalDate d3 = LocalDate.now();
        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d3);
    }

}
