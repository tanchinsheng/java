/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q610.timedate;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * The numbering for days and months starts with 1. Rest is simple math.
     * Observe that most of the methods of LocalDate (as well as LocalTime and
     * LocalDateTime) return an object of the same class. This allows you to
     * chain the calls as done in this question. However, these methods return a
     * new object. They don't modify the object on which the method is called.
     */
    public static void main(String[] args) {
        java.time.LocalDate dt1 = java.time.LocalDate.parse("2015-01-01").minusMonths(1).minusDays(1).plusYears(1);
        System.out.println(dt1);//2015-11-30
        java.time.LocalDate dt2 = java.time.LocalDate.parse("2015-01-01").minusMonths(1).minusDays(1);
        System.out.println(dt2);//2014-11-30
    }

}
