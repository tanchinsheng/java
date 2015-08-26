/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q049.javabasics;

/**
 *
 * @author cstan
 */
class User {

    Bandwidth bw = new Bandwidth();

    public void consume(int bytesUsed) {
        bw.addUsage(bytesUsed);
    }
// ... other irrelevant code    
}
