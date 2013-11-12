package part36multipleexceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In this tutorial we’ll take a look at throwing and handling multiple
 * exceptions, plus a look at a popular exam/interview question. Java 7
 * introduced a new way of handling multiple exceptions; but even if you’re
 * still on Java 6, there are things you can do to cut down the amount of work
 * in dealing with those pesky catch clauses.
 */
public class App {

    public static void main(String[] args) {
        Test test = new Test();

        /*
         try {
         test.run();
         } catch (IOException ex) {
         Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
         } catch (ParseException ex) {
         System.out.println("Could't parse command file.");
         }
         */
        /*
         try {
         test.run();
         } catch (IOException | ParseException ex) {
         Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
         }
         */
        try {
            test.run();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            test.input();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            test.input();
        } catch (FileNotFoundException | IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
