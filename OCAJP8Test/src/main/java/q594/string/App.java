/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q594.string;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static String hidePhone1(String fullPhoneNumber) {
        String mask = "xxx-xxx-";
        mask.append(fullPhoneNumber.substring(8));
        return mask;
    }

    public static String hidePhone2(String fullPhoneNumber) {
        return new StringBuilder("xxx-xxx-") + fullPhoneNumber.substring(8);
    }

    public static String hidePhone3(String fullPhoneNumber) {
        return new StringBuilder(fullPhoneNumber).replace(0, 7, "xxx-xxx-").toString();
    }

    public static String hidePhone4(String fullPhoneNumber) {
        return "xxx-xxx-" + fullPhoneNumber.substring(8, 12);
    }

    public static void main(String[] args) {
        //System.out.println(hidePhone1("ddd-ddd-dddd"));;
        System.out.println(hidePhone2("ddd-ddd-dddd"));;
        System.out.println(hidePhone3("ddd-ddd-dddd"));;
        System.out.println(hidePhone4("ddd-ddd-dddd"));;
    }

}
