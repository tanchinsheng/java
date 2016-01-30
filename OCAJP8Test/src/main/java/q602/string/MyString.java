/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q602.string;

/**
 *
 * @author cstan
 */
class MyString extends String {

    /**
     * This will not compile because String is a final class and final classes
     * cannot be extended. There are questions on this aspect in the exam and so
     * you should remember that StringBuffer and StringBuilder are also final.
     * All Primitive wrappers are also final (i.e. Boolean, Integer, Byte etc).
     * java.lang.System is also final.
     */
    MyString() {
        super();
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
