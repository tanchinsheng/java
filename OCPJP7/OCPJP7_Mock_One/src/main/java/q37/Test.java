/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q37;

class Base<T> {
}

class Derived<T> {
}

public class Test {

    public static void main(String[] args) {
        Base<? extends Number> a = new Base<Number>();
        Base<? extends Number> b = new Derived<Number>();
        Base<? extends Number> c = new Derived<Integer>();
        Derived<? extends Number> d = new Derived<Integer>();
        Base<?> e = new Derived<Integer>();
        Derived<?> f = new Derived<Integer>();
    }

}
