/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datetime;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author cstan
 */
public class NextBirthDay {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(1970, Month.SEPTEMBER, 21);

        LocalDate nextBDay = birthday.withYear(today.getYear());

        //If your birthday has occurred this year already, add 1 to the year.
        if (nextBDay.isBefore(today) || nextBDay.isEqual(today)) {
            nextBDay = nextBDay.plusYears(1);
        }

        Period p = Period.between(today, nextBDay);
        long p2 = ChronoUnit.DAYS.between(today, nextBDay);
        System.out.println("There are " + p.getMonths() + " months, and "
                + p.getDays() + " days until your next birthday. ("
                + p2 + " total)");
    }

}
