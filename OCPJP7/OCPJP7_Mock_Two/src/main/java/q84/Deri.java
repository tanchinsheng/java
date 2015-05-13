/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q84;

abstract class Base {

    public abstract Number getValue1();

    public abstract Number getValue2();

    public abstract Number getValue3();

    public abstract Number getValue4();
}

class Deri extends Base {

    protected Number getValue1() {
        return new Integer(10);
    }

    @Override
    public Integer getValue2() {
        return new Integer(10);
    }

    public Float getValue3(float flt) {
        return new Float(flt);
    }

    @Override
    public java.util.concurrent.atomic.AtomicInteger getValue4() {
        return new java.util.concurrent.atomic.AtomicInteger(10);
    }

    public static void main(String[] args) {

    }

}
