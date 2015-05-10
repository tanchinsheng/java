/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q73;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

    public static void main(String[] args) {

        String srcFile = null;
        String dstFile = null;

        try (BufferedReader inputFile = new BufferedReader(new FileReader(srcFile));
                BufferedWriter outputFile = new BufferedWriter(new FileWriter(dstFile))) {
            int ch = 0;
            while ((ch = inputFile.read()) != -1) {
                outputFile.write((char) ch);
            }
        } catch (FileNotFoundException | IOException e) {
            System.out.println("Error in opening or processing file" + e.getMessage());
        }
    }

}
