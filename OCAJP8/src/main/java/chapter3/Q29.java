/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author cstan
 */
public class Q29 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LocalDate date = LocalDate.parse("2018-04-30", DateTimeFormatter.ISO_LOCAL_DATE);
        date.plusDays(2);
        date.plusHours(3);
        System.out.println(date.getYear() + " " + date.getMonth() + " "
                + date.getDayOfMonth());
    }

}
