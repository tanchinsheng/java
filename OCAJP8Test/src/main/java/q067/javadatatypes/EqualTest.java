/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q067.javadatatypes;

/**
 *
 * @author cstan
 */
public class EqualTest {

    /**
     * Signature of equals method is : boolean equals(Object o); So it can take
     * any object. The equals methods of all wrapper classes first check if the
     * two object are of same class or not. If not, they immediately return
     * false. Hence it will print not equal.
     */
    public static void main(String args[]) {
        Integer i = new Integer(1);
        Long m = new Long(1);

        if (i.equals(m)) {
            System.out.println("equal");
        }// 1       
        else {
            System.out.println("not equal");
        }
    }
}
