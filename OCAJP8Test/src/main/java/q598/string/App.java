/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q598.string;

/**
 *
 * How can you initialize a StringBuilder to have a capacity of at least 100
 * characters?
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringBuilder sb1 = new StringBuilder(100);//true
        StringBuilder sb2 = StringBuilder.getInstance(100);
        StringBuilder sb3 = new StringBuilder();
        sb3.setCapacity(100);
        StringBuilder sb4 = new StringBuilder();
        sb4.ensureCapacity(100);//true
    }

}
