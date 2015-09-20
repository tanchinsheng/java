/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q349.methods;

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
    }

    public void showTwo(int myValue) {
        this.myValue = myValue;
    }

    public static void main(String[] args) {
        ChangeTest ct = new ChangeTest();
        ct.showTwo(200);
        System.out.println(ct.myValue);
        ct.showOne(100);
        System.out.println(ct.myValue);
    }

}
