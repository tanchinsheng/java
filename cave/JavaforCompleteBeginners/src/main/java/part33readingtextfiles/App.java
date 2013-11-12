/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part33readingtextfiles;

/**
You can use the Scanner class to simplify reading text files quite a bit. 
* In this tutorial we’ll see how to do it, and I’ll also explain how to locate 
* your file on the disk and we’ll look at a little gotcha that might catch you out
* if you want to read numbers from your file. Plus, a little insight into some 
* Java terminology, just in case you’re sufficiently young that the word “typewriter” 
* sounds to you much like “gramophone” does to me.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
 
 
public class App {
 
    public static void main(String[] args) throws FileNotFoundException {
        //String fileName = "C:/Users/John/Desktop/example.txt";
        String fileName = "example.txt";         
        File textFile = new File(fileName);    
        Scanner in = new Scanner(textFile);     
        int value = in.nextInt();
        System.out.println("Read value: " + value);    
        in.nextLine();      
        int count = 2;
        while(in.hasNextLine()) {
            String line = in.nextLine();       
            System.out.println(count + ": " + line);
            count++;
        }      
        in.close();
    }
 
}
