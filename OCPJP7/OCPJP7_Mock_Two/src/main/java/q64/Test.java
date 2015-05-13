/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q64;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) {
        String dateFormat = "d '('E')' MMM, YYYY";
        System.out.printf("%s", new SimpleDateFormat(dateFormat).format(new Date()));
    }

}
