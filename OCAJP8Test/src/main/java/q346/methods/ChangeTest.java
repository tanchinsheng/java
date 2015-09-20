/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q346.methods;

/**
 *
 * What will the following program print when run?
 */
public class ChangeTest {

    /**
     * @param args the command line arguments
     */
    private int myValue = 0;

    public void showOne(int myValue) {
        myValue = myValue;
        System.out.println(this.myValue);
    }

    public void showTwo(int myValue) {
        this.myValue = myValue;
        System.out.println(this.myValue);
    }

    public static void main(String[] args) {
        ChangeTest ct = new ChangeTest();
        ct.showOne(100);
        ct.showTwo(200);
    }

}
