/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q107.javadatatypes;

/**
 *
 * When a Noobs object is created, a MyException object is also created.
 * Therefore a total of 4 objects are created. The line Noobs c = a; just
 * assigns an existing Noobs object to c. No new object is created.
 *
 * Note: Some candidates have reported getting a similar question. The question
 * is ambiguous because two Class objects (one for Noobs and one for
 * MyException) are also created. If you consider those, then the answer would
 * be 6. If you consider the String[] object referred to by args, the answer
 * would then be 8. Further, several Thread objects are also created (although
 * not directly by this code.) Since this is out of scope for the exam, it is
 * best to ignore these kind of objects and consider only the objects created
 * directly by the code.
 */
public class Noobs {

    public Noobs() {
        try {
            throw new MyException();
        } catch (Exception e) {
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Noobs a = new Noobs();
        Noobs b = new Noobs();
        Noobs c = a;
    }

}
