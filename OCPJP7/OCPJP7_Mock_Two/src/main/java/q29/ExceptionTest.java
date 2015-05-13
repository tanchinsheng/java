/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q29;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionTest {

    public static void thrower() throws Exception {

        try {
            throw new IOException();
        } finally {
            throw new FileNotFoundException();
        }
    }

    public static void main(String[] args) {
        try {
            thrower();
        } catch (Throwable throwable) {
            System.out.println(throwable);
        }
    }

}
