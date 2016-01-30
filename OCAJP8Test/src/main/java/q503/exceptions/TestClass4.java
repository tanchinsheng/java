/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q503.exceptions;

/**
 *
 * @author cstan
 */
public class TestClass4 {

    /**
     * What can replace XXXX?
     */
    //public void myMethod() throws XXXX {
    public void myMethod() throws Throwable {
        //Because Throwable is a super class of Exception.
        throw new MyException();
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
