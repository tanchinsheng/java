 c/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q339.methods;

/**
 *
 * Which of the following options when applied individually will change the Data
 * object currently referred to by the variable d to contain 2, 2 as values for
 * its data fields?
 */


class Data {

    int x = 0, y = 0;

    public Data(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Data d = new Data(1, 1);
        //add code here
        d = new Data(2, 2); // This will create a new Data object and will not change the original Data object referred to be d.
        d.x = 2;//Correct
        d.y = 2;
        d.x += 1;//Correct
        d.y += 1;
        d = d + 1;//This will not compile because Java does not allow operator overloading for user defined objects.
    }

}
