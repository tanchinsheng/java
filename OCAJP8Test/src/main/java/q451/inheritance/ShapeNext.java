/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q451.inheritance;

/**
 *
 * @author cstan
 */
public interface ShapeNext {

    public abstract void draw();
}
//By default all the methods of an interface are public and abstract so there is no need to explicitly
//specify the "abstract" keyword for the draw() method if you make Shape an interface. But it is not wrong to do so.
