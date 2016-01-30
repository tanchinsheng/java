/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q566.string;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringBuilder b1 = new StringBuilder("snorkler");
        StringBuilder b2 = new StringBuilder("yoodler");

        b1.append(b2.substring(2, 5).toUpperCase());
        System.out.println("b1= " + b1 + ", b2= " + b2);//b1= snorklerODL, b2= yoodler

        StringBuilder b3 = new StringBuilder("snorkler");
        StringBuilder b4 = new StringBuilder("yoodler");
        b4.insert(3, b3.append("a"));
        System.out.println("b3= " + b3 + ", b4= " + b4);//b3= snorklera, b4= yoosnorkleradler

        StringBuilder b5 = new StringBuilder("snorkler");
        StringBuilder b6 = new StringBuilder("yoodler");

        b5.replace(3, 4, b6.substring(4)).append(b6.append(false));
        System.out.println("b5= " + b5 + ", b6= " + b6);

        b6.append("a").substring(0, 4).insert(2, "asdf");//invalid
        String str = b6.append("a").insert(2, "asdf").substring(0, 4);//Valid

    }

}
