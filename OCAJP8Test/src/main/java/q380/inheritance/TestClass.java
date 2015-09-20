/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q380.inheritance;

/**
 *
 * @author cstan
 */
class Base {

    public Object getValue() {
        return new Object();
    } //1
}

class Base2 extends Base {

    public String getValue() {
        return "hello";
    }
} //2

public class TestClass {

    /**
     * Observe that at run time b points to an object of class Base2. Further,
     * Base2 overrides getValue(). Therefore, Base2's getValue() will be invoked
     * and it will return hello.
     */
    public static void main(String[] args) {
        Base b = new Base2();
        System.out.println(b.getValue()); //3
        // Covariant returns are allowed since Java 1.5, which means that an overriding method can change the
        // return type to a subclass of the return type declared in the overridden method.
        // But remember than covarient returns does not apply to primitives.
    }

}
