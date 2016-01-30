/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q614.timedate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public String getDateString(LocalDateTime ldt) {
        return DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ldt);
    }

    public static void main(String[] args) {
        System.out.println(new App().getDateString(LocalDateTime.now()));
    }

}
