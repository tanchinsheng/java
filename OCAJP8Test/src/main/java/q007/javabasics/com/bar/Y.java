/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q007.javabasics.com.bar;

/**
 *
 * @author cstan
 */
//1 <== INSERT STATEMENT(s) HERE
import q007.javabasics.com.foo.X;
import static q007.javabasics.com.foo.X.LOGICID;

public class Y {

    public static void main(String[] args) {
        X x = new X();
        x.apply(LOGICID);
    }
}
