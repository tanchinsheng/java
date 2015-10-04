/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q420.inheritance;

/**
 *
 * Which statements about the following code contained in BankAccount.java are
 * correct?
 */
interface Account {

    public default String getId() {
        return "0000";
    }
}

interface PremiumAccount extends Account {

    public String getId();
}

public class BankAccount implements PremiumAccount {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Account acct = new BankAccount();
        System.out.println(acct.getId());
    }

//    @Override
//    public String getId() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
