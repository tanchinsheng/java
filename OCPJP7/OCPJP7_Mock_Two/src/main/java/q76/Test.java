/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q76;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Test {

    public static void main(String[] args) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2012);
        c.set(Calendar.MONTH, 12); // 0..11~Jan..Dec
        c.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(df.format(c.getTime()));
    }

}
