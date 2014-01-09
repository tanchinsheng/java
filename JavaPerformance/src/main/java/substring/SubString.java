/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package substring;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SubString {

    public static void main(String[] args) throws Throwable {

        Runtime rt = Runtime.getRuntime();
        double alloc = rt.totalMemory() / 1000.0;
        double free = rt.freeMemory() / 1000.0;

        System.out.printf("Allocated (before substring operation) : %.2f kb\nFree: %.2f kb\n\n", alloc, free);
        Scanner in = new Scanner(new File("my_file.txt"));
        ArrayList<String> al = new ArrayList<String>();

        while (in.hasNextLine()) {
            String s = in.nextLine();
            System.out.printf("\n" + s + "\n");
            /* Problem Code */
            //al.add(s.substring(0, 2)); // extracts first 200 characters
            /* Solution Code */
            al.add(new String(s.substring(0,2))); 
        }

        alloc = rt.totalMemory() / 1000.0;
        free = rt.freeMemory() / 1000.0;
        System.out.printf("\nAllocated (after substring operation): %.2f kb\nFree: %.2f kb\n\n", alloc, free);

        in.close();
        System.gc();

        alloc = rt.totalMemory() / 1000.0;
        free = rt.freeMemory() / 1000.0;
        System.out.printf("\nAllocated (after System.gc() operation):: %.2f kb\nFree: %.2f kb\n\n", alloc, free);
    }
}
