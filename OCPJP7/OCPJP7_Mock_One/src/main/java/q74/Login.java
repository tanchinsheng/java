/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q74;

import java.io.Console;

/**
 *
 * @author cstan
 */
public class Login {

    public static void main(String[] args) {
        Console console = System.console();
        String userName = null;
        char[] password = null;
        if (console != null) {
            userName = console.readLine("Enter your username: ");
            password = console.readPassword("Enter password: ");
            System.out.println(userName + " ," + password);
        }
    }

}
