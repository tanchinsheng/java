/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q15;

class Base {
}

class DeriOne extends Base {
}

class DeriTwo extends Base {
}

class ArrayStore {

    public static void main(String[] args) {
        Base[] baseArr = new DeriOne[3];
        baseArr[0] = new DeriOne();
        // an object of type DeriTwo is assigned to the type DeriOne,no parent-child relation.
        baseArr[2] = new DeriTwo();
        System.out.println(baseArr.length);
    }

}
