/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4;

public class Q05 {

    public static int howMany(boolean b, boolean... b2) {
        return b2.length;
    }

    public static void main(String[] args) {
        howMany();
        howMany(true);
        howMany(true, true);
        howMany(true, true, true);
        howMany(true, {true
        });
        howMany(true, {true
        , true});
        howMany(true, new boolean[2]);
    }

}
