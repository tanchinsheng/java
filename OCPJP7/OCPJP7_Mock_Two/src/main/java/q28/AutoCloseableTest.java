/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q28;

import java.util.Scanner;

public class AutoCloseableTest {

    public static void main(String[] args) {
        try (Scanner consoleScanner = new Scanner(System.in)) {
            consoleScanner.close();
            consoleScanner.close();
        }
    }

}
