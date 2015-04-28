/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q15;

/**
 *
 * @author cstan
 */
public class Outer {

    private final int mem = 10;
    protected int pmem;

    public Outer() {
        this.pmem = 10;
    }

    class Inner {

        private final int imem = new Outer().mem;
    }

    public static void main(String[] args) {
        System.out.println(new Outer().new Inner().imem);
        System.out.println(new Outer().pmem);
    }

}
