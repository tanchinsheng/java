/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q457.inheritance.p2;

/**
 *
 * @author cstan
 */
import q457.inheritance.p1.Movable;

public class Donkey implements Movable {

    int location = 200;

    public void move(int by) {
        location = location + by;
    }

    public void moveBack(int by) {
        location = location - by;
    }
}
