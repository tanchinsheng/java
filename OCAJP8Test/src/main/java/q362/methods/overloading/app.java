/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q362.methods.overloading;

/**
 *
 * Given the following pairs of method declarations, which of the statements are
 * true?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    void perform_work_1(int time) {
    }

    int perform_work_1(int time, int speed) {
        return time * speed;
    }

    void perform_work_2(int time) {
    }

    int perform_work_2(int speed) {
        return speed;
    } //You cannot have two methods with the same signature (i.e. same name and same parameter list) in the same class.
    // Note that return type and names of the parameters don't matter while determining the signature.

    void perform_work_3(int time) {
    }

    void Perform_work_3(int time) {
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
