/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class Test1 {

    public static void main(String[] args) {
        System.out.printf("%s%n", DayOfWeek.MONDAY.plus(3));

        DayOfWeek dow = DayOfWeek.MONDAY;
        Locale locale = Locale.getDefault();
        System.out.println(dow.getDisplayName(TextStyle.FULL, locale));
        System.out.println(dow.getDisplayName(TextStyle.NARROW, locale));
        System.out.println(dow.getDisplayName(TextStyle.SHORT, locale));

        System.out.printf("%d%n", Month.FEBRUARY.maxLength());
        Month month = Month.AUGUST;
        System.out.println(month.getDisplayName(TextStyle.FULL, locale));
        System.out.println(month.getDisplayName(TextStyle.NARROW, locale));
        System.out.println(month.getDisplayName(TextStyle.SHORT, locale));

        LocalDate date = LocalDate.of(2000, Month.NOVEMBER, 20);
        TemporalAdjuster adj = TemporalAdjusters.next(DayOfWeek.WEDNESDAY);
        LocalDate nextWed = date.with(adj);
        System.out.printf("For the date of %s, the next Wednesday is %s. %n", date, nextWed);

        YearMonth ym = YearMonth.now();
        System.out.printf("%s: %d%n", ym, ym.lengthOfMonth());

        YearMonth ym2 = YearMonth.of(2010, Month.FEBRUARY);
        System.out.printf("%s: %d%n", ym2, ym2.lengthOfMonth());

        YearMonth ym3 = YearMonth.of(2012, Month.FEBRUARY);
        System.out.printf("%s: %d%n", ym3, ym3.lengthOfMonth());

        MonthDay md = MonthDay.of(Month.FEBRUARY, 29);
        boolean validLeapYear = md.isValidYear(2010);
        System.out.printf("%s: %b%n", md, validLeapYear);

    }

}
