/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4.pond.shore;

public class Bird {

    protected String text = "floating"; // protected access

    protected void floatInWater() { // protected access
        System.out.println(text);
    }
}
