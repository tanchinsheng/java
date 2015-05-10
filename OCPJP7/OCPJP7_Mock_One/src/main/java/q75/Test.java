/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q75;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.PushbackReader;

public class Test {

    public static void main(String[] args) {
        String file = null;
        try (
                BufferedReader inputFile = new BufferedReader(new FileReader(file))) {

        } catch (IOException ex) {
        }
        try (
                FileReader inputFile = new FileReader(file)) {

        } catch (IOException ex) {
        }
        try (
                FilterReader inputFile = new FilterReader(file)) { // abstract class

        } catch (IOException ex) {
        }
        try (
                FilterReader inputFile = new PushbackReader(new FileReader(file))) { // abstract class

        } catch (IOException ex) {
        }

    }

}
