/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q290.loop;

/**
 *
 * What will the following code print when compiled and run?
 */
public class DaysTest {

    /**
     * @param args the command line arguments
     */
    static String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};

    public static void main(String[] args) {
        int index = 0;
        for (String day : days) {
            if (index == 3) {
                break;
            } else {
                continue;
            }
            index++;
            if (days[index].length() > 3) {
                days[index] = day.substring(0, 3);
            }
        }
        System.out.println(days[index]);
    }
}
