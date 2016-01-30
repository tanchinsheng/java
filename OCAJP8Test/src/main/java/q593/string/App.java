/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q593.string;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static String hidePhone1(String fullPhoneNumber) {
        return new StringBuilder(fullPhoneNumber).substring(0, 8) + "xxxx";//ddd-ddd-xxxx
    }

    public static String hidePhone2(String fullPhoneNumber) {
        return new StringBuilder(fullPhoneNumber).replace(8, 12, "xxxx").toString();//ddd-ddd-xxxx
    }

    public static String hidePhone3(String fullPhoneNumber) {
        return new StringBuilder(fullPhoneNumber).append("xxxx", 8, 12).toString();
        //Exception in thread "main" java.lang.IndexOutOfBoundsException: start 8, end 12, s.length() 4
    }

    public static String hidePhone4(String fullPhoneNumber) {
        return new StringBuilder("xxxx").append(fullPhoneNumber, 0, 8).toString();//xxxxddd-ddd-
    }

    public static String hidePhone5(String fullPhoneNumber) {
        return new StringBuilder("xxxx").insert(0, fullPhoneNumber, 0, 8).toString();//ddd-ddd-xxxx
    }

    public static void main(String[] args) {
        System.out.println(hidePhone1("ddd-ddd-dddd"));;
        System.out.println(hidePhone2("ddd-ddd-dddd"));;
        //System.out.println(hidePhone3("ddd-ddd-dddd"));;
        System.out.println(hidePhone4("ddd-ddd-dddd"));;
        System.out.println(hidePhone5("ddd-ddd-dddd"));;
    }

}
