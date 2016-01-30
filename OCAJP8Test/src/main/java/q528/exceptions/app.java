/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q528.exceptions;

/**
 *
 * A new Java programmer has written the following method that takes an array of
 * integers and sums up all the integers that are less than 100.
 */
public class app {

    /**
     * Which of the following are best practices to improve this code?
     *
     * @param values
     */
    public void processArray(int[] values) {
        int sum = 0;
        int i = 0;
        try {
            while (values[i] < 100) {
                sum = sum + values[i];
                i++;
            }
        } catch (Exception e) {
        }
        System.out.println("sum = " + sum);
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
