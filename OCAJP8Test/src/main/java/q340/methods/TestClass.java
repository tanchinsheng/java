/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q340.methods;

/**
 *
 * Which of the following options when applied individually will change the Data
 * object currently referred to by the variable d to contain 2, 2 as values for
 * its data fields?
 */
class Data {

    private int x = 0, y = 0;// now is private

    public Data(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setValues(int x, int y) {
        this.x = x;
        this.y = y;
    }
    // This is a good example of encapsulation where the data members of Data class are private and
    // there is a method in Data class to manipulate its data.
    // Compare this approach to making x and y as public and letting other classes directly modify the values.

}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Data d = new Data(1, 1);
        //add code here
        d.setValues(2, 2);
    }

}
