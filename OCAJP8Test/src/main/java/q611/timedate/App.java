/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q611.timedate;

import java.time.LocalDateTime;

/**
 *
 * Which of the following lines will return the date string in ISO 8601 format?
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LocalDateTime dt = LocalDateTime.parse("2015-01-02T17:13:50");

        System.out.println(dt.format(java.time.format.DateTimeFormatter.ISO_DATE_TIME));
        System.out.println(dt.toString());
        //LocalDateTime's toString method generates the String in ISO 8601 format.

        dt.format(java.time.format.DateTimeFormatter.DATE_TIME); //DATE_TIME is not a valid formatter.
        dt.format(java.time.format.DateTimeFormatter.LOCAL_DATE_TIME);
        //LOCAL_DATE_TIME is not a valid formatter. ISO_LOCAL_DATE_TIME is valid though,
        //which is same as ISO_DATE_TIME except that it does not use the Zone or Offset.
        //Details are not too important for the exam but you may check out the JavaDoc
        //descriptions of both as they have good examples.

    }

}
