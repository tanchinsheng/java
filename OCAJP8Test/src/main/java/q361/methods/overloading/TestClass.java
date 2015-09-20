/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q361.methods.overloading;

/**
 *
 * What will it print when compiled and run?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
//    void probe(int... x) {
//        System.out.println("In ...");
//    }  //1
//
//    void probe(Integer x) {
//        System.out.println("In Integer");
//    } //2
    void probe(long x) {
        System.out.println("In long");
    } //3

    void probe(Long x) {
        System.out.println("In LONG");
    } //4

    public static void main(String[] args) {
        Integer a = 4;
        new TestClass().probe(a); //5
        int b = 4;
        new TestClass().probe(b); //6
    }

}
