/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q263.loop;

/**
 *
 * How many times will the output contain 2?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] dataList = {"x", "y", "z"};
        for (String dataElement : dataList) {
            int innerCounter = 0;
            while (innerCounter < dataList.length) {
                System.out.println(dataElement + ", " + innerCounter);
                innerCounter++;
            }
        }
    }
}
