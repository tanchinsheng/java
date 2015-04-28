/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q36;

class Base<T> {
}

class Derived<T> {
}

public class Test {

    public static void main(String[] args) {
        Base<Number> a = new Base<Number>();
        Base<Number> b = new Derived<Number>();
        Base<Number> c = new Derived<Integer>();
        Derived<Number> d = new Derived<Integer>();
        Based<Integer> e = new Derived<Integer>();
        Derived<Integer> f = new Derived<Integer>();
    }

}
