/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q616.timedate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY)));
        System.out.println(LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY)));
        System.out.println(TemporalAdjusters.next(DayOfWeek.TUESDAY).adjustInto(LocalDate.now()));
        System.out.println(new LocalDate().with(TemporalAdjusters.next(DayOfWeek.TUESDAY)));
        System.out.println(new LocalDate().adjust(TemporalAdjusters.next(DayOfWeek.TUESDAY)));

    }

}
