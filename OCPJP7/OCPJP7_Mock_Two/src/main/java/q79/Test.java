/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q79;

class Base {

    public void print() {
        System.out.println("Base");
    }
}

class Derived extends Base {

    @Override
    public void print() {
        System.out.println("Derived");
    }
}

class Test {

    public static void main(String[] args) {
        Base obj1 = new Derived();
        Base obj2 = (Base) obj1;
        obj1.print();
        // dynamic type of the instance variable remains the same
        obj2.print();
    }

}
