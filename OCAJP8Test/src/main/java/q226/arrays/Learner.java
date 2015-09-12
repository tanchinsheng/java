/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q226.arrays;

/**
 *
 * What is the result of compiling and running the following program?
 */
public class Learner {

    /**
     * Array indexing starts with 0. The first element therefore is at
     * dataArr[0], which is not set in this code. It is initialized by default
     * to null. Hence, the code prints null Bill Steve Larry.
     */
    public static void main(String[] args) {
        String[] dataArr = new String[4];
        dataArr[1] = "Bill";
        dataArr[2] = "Steve";
        dataArr[3] = "Larry";
        try {
            for (String data : dataArr) {
                System.out.print(data + " ");
            }
        } catch (Exception e) {
            System.out.println(e.getClass());
        }
    }
}
