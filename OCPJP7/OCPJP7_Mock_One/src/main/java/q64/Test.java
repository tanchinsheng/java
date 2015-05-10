/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q64;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

    public static void main(String[] files) {

        int ch = 0; // not char
        final int VAL = -1;// end of streams
        try (FileWriter outputFile = new FileWriter(files[1]);
                FileReader inputFile = new FileReader(files[0])) {
            while ((ch = inputFile.read()) != VAL) {
                outputFile.write((char) ch);
            }
        } catch (IOException ex) {
        }

    }
}
