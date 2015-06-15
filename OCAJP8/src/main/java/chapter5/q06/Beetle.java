/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q06;

interface HasExoskeleton {

    abstract int getNumberOfSections();
}

abstract class Insect implements HasExoskeleton {

    abstract int getNumberOfLegs();
}

public class Beetle extends Insect {

    @Override
    int getNumberOfLegs() {
        return 6;
    }
}
