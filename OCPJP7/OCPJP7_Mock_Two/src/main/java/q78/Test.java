/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q78;

class Test {

    Integer I;
    //Integer I = 0;
    int i;

    public Test(int i) {
        this.i = I + i;
        System.out.println(this.i);
    }

    public static void main(String[] args) {
        Integer I = new Integer(1);
        Test test = new Test(I);
    }

}
