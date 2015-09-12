/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q189.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * Note that if and else do not cascade. They are like opening an closing
     * brackets. So, else at //4 is associated with if at //3 and else at //5 is
     * associated with if at //2
     */
    public static void ifTest(boolean flag) {
        if (flag) //1
        {
            if (flag) //2
            {
                if (flag) //3
                {
                    System.out.println("False True");
                } else //4
                {
                    System.out.println("True False");
                }
            } else //5
            {
                System.out.println("True True");
            }
        } else //6
        {
            System.out.println("False False");
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
