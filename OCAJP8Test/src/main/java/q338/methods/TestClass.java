/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q338.methods;

/**
 *
 * What will the following code print when compiled and run:
 */
class Data {

    int intVal = 0;
    String strVal = "default";

    public Data(int k) {
        this.intVal = k;
    }

}

public class TestClass {

    /**
     * This is quite straight forward question. You are creating only one Data
     * object. You are setting its strVal field to "D1". Next, you declare
     * another Data variable d2 and assign to it the same Data object.      *
     * Thus, when you access strVal using d2, you will get D1.
     *
     * The "throws Exception" part is not required and is there just to confuse
     * you.
     */
    public static void main(String[] args) throws Exception {
        Data d1 = new Data(10);
        d1.strVal = "D1";
        Data d2 = d1;
        d2.intVal = 20;
        System.out.println("d2 val = " + d2.strVal);
    }

}
