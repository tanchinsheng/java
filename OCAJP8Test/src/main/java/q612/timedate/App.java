/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q612.timedate;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * Observe that you are creating a LocalDate and not a LocalDateTime.
     * LocalDate doesn't have time component and therefore, you cannot format it
     * with a formatter that expects time component such as
     * DateTimeFormatter.ISO_DATE_TIME. Thus, it will print
     * java.time.temporal.UnsupportedTemporalTypeException: Unsupported field:
     * HourOfDay exception message. If you use DateTimeFormatter.ISO_DATE, it
     * will print 2015-01-31 . Also, remember that a LocalDateTime object can be
     * formatted using a DateTimeFormatter.ISO_DATE though.
     */
    public static void main(String[] args) {
        System.out.println(LocalDate.of(2015, Month.JANUARY, 31).format(DateTimeFormatter.ISO_DATE_TIME));
        //Exception in thread "main" java.time.temporal.UnsupportedTemporalTypeException: Unsupported field: HourOfDay
    }

}
