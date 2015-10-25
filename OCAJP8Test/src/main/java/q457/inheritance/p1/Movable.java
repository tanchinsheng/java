/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q457.inheritance.p1;

/**
 *
 * @author cstan
 */
public interface Movable {

    int location = 0;

    void move(int by);

    public void moveBack(int by);
}
