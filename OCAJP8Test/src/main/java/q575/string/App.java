/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q575.string;

/**
 *
 * Which of these expressions will return true?
 *
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if ("hello world".equals("hello world")) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        if ("HELLO world".equalsIgnoreCase("hello world")) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        if ("hello".concat(" world").trim().equals("hello world")) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        System.out.println("hello world".compareTo("Hello world"));
        if ("hello World".compareTo("Hello world") < 0) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        if ("Hello world".toLowerCase().equals("hello world")) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

    }

}
