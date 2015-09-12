/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q279.loop;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        int k = 0;
        int m = 0;
        for (int i = 0; i <= 3; i++) {
            System.out.println("A.i=" + i + ",k=" + k + ",m=" + m);
            //1.i=0,k=0,m=0
            //3.i=1,k=1,m=1
            //5.i=2,k=2,m=2
            //8.i=3,k=3,m=4
            k++;
            if (i == 2) {
                // line 1
                // break; // 3,2
                // continue; // 4,3
                // i = m++; //4,5
                System.out.println("C.i=" + i + ",k=" + k + ",m=" + m);
                //6.i=2,k=3,m=3
                // i=4;
            }
            m++;
            System.out.println("B.i=" + i + ",k=" + k + ",m=" + m);
            //2.i=0,k=1,m=1
            //4.i=1,k=2,m=2
            //7.i=2,k=3,m=4
            //9.i=3,k=4,m=5
        }
        System.out.println(k + ", " + m);
    }

}
