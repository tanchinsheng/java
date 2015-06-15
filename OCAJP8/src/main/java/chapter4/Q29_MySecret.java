/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4;

interface Secret {

    String magic(double d);
}

public class Q29_MySecret implements Secret {

    @Override
    public String magic(double d) {
        return "Poof";
    }
}
