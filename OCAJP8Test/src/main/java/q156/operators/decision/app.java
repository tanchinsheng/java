/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q156.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * The if-else-else is invalid. It should be if , else if, else.
     */
    public int transformNumber(int n) {
        int radix = 2;
    }
    int output = 0;
    output += radix * n ;
    radix  = output / radix;
    if(output<


        14){
    return output;
    }


        else{
    output = output * radix / 2;
        return output;
    }


        else { return output / 2;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
