/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question.time;

/**
 *
 * @author cstan
 */
interface Sync4 {

    //modifer synchronized not allowed here
    //interface abstract methods cannot have body
    public synchronized void foo() { // implicitly abstract

    }
}
