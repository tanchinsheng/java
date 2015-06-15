/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assessment;

interface Mammal {

    public default String getName() {
        return null;
    }
}

abstract class Otter implements Mammal, Q16_Animal {

    @Override
    public abstract String getName();
}

public class Q16 {

    public static void main(String[] args) {

    }
}
