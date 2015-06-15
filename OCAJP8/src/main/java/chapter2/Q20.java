/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

/**
 *
 * @author cstan
 */
public class Q20 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final char a = 'A', d = 'D';
        char grade = 'B';
        switch (grade) {
            case a:
            case 'B':
                System.out.println("great");
            case 'C':
                System.out.println("good");
                break;
            case d:
            case 'F':
                System.out.println("not good");
        }
    }

}
