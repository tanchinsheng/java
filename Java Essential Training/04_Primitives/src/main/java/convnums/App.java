/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convnums;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double doubleValue = 3.99;
        int intResult = (int) doubleValue;
        System.out.println(intResult);

        double doublePrimitive = 3.99;
        // Double doubleObj = new Double(doublePrimitive);
        Double doubleObj = doublePrimitive;
        int intPrimitive = doubleObj.intValue();
        System.out.println(intPrimitive);

    }

}
