/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q332.methods;

/**
 *
 * Which of the following is the correct way to make the variable 'id' read only
 * for any other class?
 */
public class Test {

    /**
     * This is a standard way of providing read only access to internal
     * variables.
     */
    public int id; // This will not allow others to read or write.

    public int getId() {
        return id;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
