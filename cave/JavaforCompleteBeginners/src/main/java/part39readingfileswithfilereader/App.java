/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part39readingfileswithfilereader;

/**
 * The most flexible way to read files in Java involves using a sort of Russian
 * doll system of objects; in this tutorial we’ll look at reading text files
 * using such a method. Unfortunately prior to Java 7 we end up using a bizarre
 * and byzantine system of exceptions too, but Java 7 fixes that, as we’ll see
 * in the next tutorial.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class App {

    public static void main(String[] args) {
        File file = new File("test.txt1");
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line;
//            while( (line = br.readLine()) != null ) {
//                System.out.println(line);
//            }  
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            System.out.println(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.toString());
        } catch (IOException e) {
            System.out.println("Unable to read file: " + file.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Unable to close file: " + file.toString());
            } catch (NullPointerException ex) {
                // File was probably never opened!
            }
        }
    }
}
