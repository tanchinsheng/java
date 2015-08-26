/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q089.javadatatypes;

/**
 *
 * @author cstan
 */
public class SomeClass {

    /**
     * The problem is that local is declared inside a method is therefore local
     * to that method. It is called a local variable (also known as automatic
     * variable) and it cannot be used before initialized. Further, it will not
     * be initialized unless you initialize it explicitly because local
     * variables are not initialized by the JVM on its own. The compiler spots
     * the usage of such uninitialized variables and ends with an error message.
     * 1. Making it a member variable (choice "Move line 1 and place it after
     * line 0.") will initialize it to null. 2. Putting an else part (choice
     * "Insert after line 2 : else local = "bad";") will ensure that it is
     * initialized to either 'good' or 'bad'. So this also works. Choice "Insert
     * after line 2 : if(n <= 0) local = "bad";" doesn't work because the second
     * 'if' will actually be another statement and is not considered as a part
     * of first 'if'. So, compiler doesn't realize that 'local' will be
     * initialized even though it does get initialized.
     */
    String s1 = "green mile"; // 0    
    String local; // 1

    public void generateReport(int n) {
        // String local; // 1
        if (n > 0) {
            local = "good";//2
        }
        /* compile
         else {
         local = "bad";
         }
         */
        System.out.println(s1 + " = " + local);//3
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
