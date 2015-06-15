/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4.pond.goose;

import chapter4.pond.shore.Bird; // in a different package

public class Gosling extends Bird { // extends means create subclass

    public void swim() {
        floatInWater(); // calling protected member
        System.out.println(text); // calling protected member
    }
}
