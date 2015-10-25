/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q442.inheritance;

/**
 *
 * What can be inserted at //1 and //2 in the code below so that it can compile
 * without errors:
 */
class Doll {

    String name;

    Doll(String nm) {
        this.name = nm;
    }
}

class Barbie extends Doll {

    Barbie() {
        //1
        //super("unknown");
        this("unknown");
    }

    Barbie(String nm) {
        //2
        super(nm);

    }
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Barbie b = new Barbie("mydoll");
    }

}
