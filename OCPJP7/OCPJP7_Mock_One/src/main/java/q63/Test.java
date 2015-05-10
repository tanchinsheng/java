/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q63;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Test {

    public static void main(String[] files) {
        try (FileReader inputFile = new FileReader(new File(files[0]))) {

        } catch (FileNotFoundException | IOException ex) {
            ex.printStackTrace();
        }
    }

}
