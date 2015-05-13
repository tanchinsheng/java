/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q20;

import java.util.regex.Pattern;

public class Split {

    public static void main(String[] args) {
        String date = "10-01-2012"; // 10th January 2012 in dd-mm-yyyy format
        String[] dateParts = date.split("-");
        System.out.println("Using String.split method: ");
        for (String part : dateParts) {
            System.out.print(part + " ");
        }

        System.out.println("\nUsing regex pattern: ");
        Pattern datePattern = Pattern.compile("-");
        dateParts = datePattern.split(date);
        for (String part : dateParts) {
            System.out.print(part + " ");
        }
    }

}
