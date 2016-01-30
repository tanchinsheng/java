/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q609.timedate;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.time.LocalDate.parse("2015-01-02");

        java.time.LocalDateTime.parse("2015-01-02");

        java.time.LocalDateTime.of(2015, 10, 1, 10, 10);

        java.time.LocalDateTime.of(2015, "January", 1, 10, 10);
    }

}
