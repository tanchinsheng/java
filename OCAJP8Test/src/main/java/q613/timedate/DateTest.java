/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q613.timedate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author cstan
 */
public class DateTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {//1
        LocalDateTime greatDay = LocalDateTime.parse("2015-01-01");//2  
        //Exception in thread "main" java.time.format.DateTimeParseException: Text '2015-01-01' could not be parsed at index 10       
        String greatDayStr = greatDay.format(DateTimeFormatter.ISO_DATE_TIME); //3        
        System.out.println(greatDayStr);//4
    }

}
