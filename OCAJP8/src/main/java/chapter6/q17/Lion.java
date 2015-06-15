/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter6.q17;

class HasSoreThroatException extends Exception {
}

class TiredException extends RuntimeException {
}

interface Roar {

    void roar() throws HasSoreThroatException;
}

class Lion implements Roar {// INSERT CODE HERE

    @Override
    // public void roar() { //OK
    // public void roar() throws HasSoreThroatException { //OK
    // public void roar() throws Exception{ // Overridden medthod does not throw Exception
    // public void roar() throws IllegalArgumentException { // RuntimeException need not be handled
    public void roar() throws TiredException {  // RuntimeException need not be handled
    }

}
