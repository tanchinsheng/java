/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q094.javadatatypes;

/**
 *
 * The rules are: 1. static variables can be left without being explicitly
 * initialized. (They will get default values). 2. final variables must be
 * explicitly initialized. Now, here CLASS_GUID is a 'static final' variable and
 * not just final or static. As static fields can be accessed even without
 * creating an instance of the class, it is entirely possible that this field
 * can be accessed before even a  single instance is created. In this case, no
 * constructor or non-static initializer had ever been called. And so, the field
 * (as it is final and so must be initialized explicitly) remains uninitialized.
 * This causes the compiler to complain. Had CLASS_GUID been just a final
 * variable, option 4 would work but as it is also static, it cannot wait till
 * the constructor executes to be initialized.
 */
public class Widget {

    static int MAX;     //1
    static final String CLASS_GUID;   // 2

    static {
        MAX = 111;
        CLASS_GUID = "XYZ123";
    }

    Widget() {
        //3
    }

    Widget(int k) {
        //4
    }
}
