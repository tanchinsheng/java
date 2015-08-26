/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q104.javadatatypes;

/**
 *
 * String, StringBuilder, and StringBuffer - all are final classes. 1. Remember
 * that wrapper classes for primitives (java.lang.Boolean, java.lang.Integer,
 * java.lang.Long, java.lang.Short etc.) are also final and so they cannot be
 * extended. 2. java.lang.Number, however, is not final. Integer, Long, Double
 * etc. extend Number. 3. java.lang.System is final as well.
 */
public class ThreeWayBoolean extends Boolean { // Boolean is final

    public ThreeWayBoolean(boolean value) {
        super(value);
    }
    //implementation for abstract methods of the base class
}
