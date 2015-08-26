/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q082.javadatatypes;

/**
 *
 * @author cstan
 */
public class Bandwidth {

    /**
     * @param args the command line arguments
     */
    public int available = 0;

    public int getAvailable() {
        return available;
    }

    public Bandwidth(int quota) {
        this.available = quota;
    }

    public void addMore(int more) {
        available += more;
    }

    public static void main(String[] args) {
        Bandwidth bw = new Bandwidth(100);
        //INSERT CODE HERE
        // bw.available = 0; // print 0
        bw.addMore(-bw.getAvailable()); // print 0
        System.out.println(bw.getAvailable());
    }

}
