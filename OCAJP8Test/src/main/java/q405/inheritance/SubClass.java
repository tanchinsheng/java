/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q405.inheritance;

/**
 *
 * What will be printed when the following program is compiled and run?
 *
 */
class Super {

    public int getNumber(int a) {
        return 2;
    }
}

public class SubClass extends Super {

    /**
     * Note that the parameters of SubClass's getNumber are different than
     * Super's getNumber. So it is not overriding it. So the super class's
     * getNumber() will be called which returns 2.
     */
    public int getNumber(int a, char ch) {
        return 4;
    }

    public static void main(String[] args) {
        System.out.println(new SubClass().getNumber(4)); //2
    }

}
