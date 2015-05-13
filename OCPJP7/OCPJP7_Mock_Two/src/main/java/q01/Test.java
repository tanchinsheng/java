/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q01;

class Base {

    public void print() {
        System.out.println("Base:print");
    }
}

abstract class Test extends Base {

    public static void main(String[] args) {
        Base obj = new Base();
        obj.print();
    }

}
