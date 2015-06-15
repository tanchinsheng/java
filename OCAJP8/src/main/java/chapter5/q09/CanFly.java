/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q09;

public interface CanFly {

    void fly();
}

interface HasWings {

    public abstract Object getWindSpan();
}

abstract class Falcon implements CanFly, HasWings {
}
