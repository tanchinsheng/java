/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q04;

import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

public class Format {

    public static void main(String[] args) {
        Formatter formatter = new Formatter();
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.set(/* year =*/2012, /*month = */ Calendar.FEBRUARY, /*date = */ 1);
        formatter.format("%tY/%<tB/%<td", calendar);
        System.out.println(formatter);

        Formatter formatter2 = new Formatter();
        formatter2.format("%tY/%tB/%td", calendar, calendar, calendar);
        System.out.println(formatter2);
    }

}
