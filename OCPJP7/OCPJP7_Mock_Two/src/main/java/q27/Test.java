/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q27;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

    public static void main(String[] args) {

        try (BufferedReader br1 = new BufferedReader(System.in)) {
            String str1 = br1.readLine();
        } catch (IOException ex) {
        }

        try (BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in))) {
            String str2 = br2.readLine();
        } catch (IOException ex) {
        }

        InputStreamReader isr = new InputStreamReader(new BufferedReader(System.in));
        String str3 = isr.readLine();

        String str4 = System.in.readLine();

        String str5;
        System.in.scanf(str5);

    }

}
