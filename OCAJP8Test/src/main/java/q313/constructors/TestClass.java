/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q313.constructors;

/**
 *
 * Which line contains a valid constructor in the following class definition?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    int i, j;

    public TestClass getInstance() {
        return new TestClass();
    }  //1

    public void TestClass(int x, int y) {
        i = x;
        j = y;
    }     //2

    public TestClass TestClass() {
        return new TestClass();
    }    //3
    public

    ~TestClass() {
    }                     //4

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
