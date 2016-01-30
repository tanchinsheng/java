/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q049.javabasics.oo;

/**
 *
 * @author cstan
 */
public class Bandwidth {

    private int totalUsage;
    private double totalBill;
    private double costPerByte;

    //add your code here
    public void addUsage1(int bytesUsed) {
        if (bytesUsed > 0) {
            totalUsage = totalUsage + bytesUsed;
            totalBill = totalBill + bytesUsed * costPerByte;
        }
    }

    protected void addUsage2(int bytesUsed) {
        totalUsage += bytesUsed;
        totalBill = totalBill + bytesUsed * costPerByte;
    } //There is no validity check for bytesUsed argument.
    // User will be able to tamper will the bill by suppling a negative number for bytesUsed.
    // There is no validity check for bytesUsed argument. User will be able to tamper will the bill by suppling
    // a negative number for bytesUsed.

    private void addUsage3(int bytesUsed) {
        if (bytesUsed > 0) {
            totalUsage = totalUsage + bytesUsed;
            totalBill = totalUsage * costPerByte;
        }
    }//If this method is made private, User class will not be able to access it.

    public void addUsage4(int bytesUsed) {
        if (bytesUsed > 0) {
            totalUsage = totalUsage + bytesUsed;
        }
    }

    public void updateTotalBill() {
        totalBill = totalUsage * costPerByte;
    } //This is not a good approach because once the User class calls addUsage() method,
    //totalBill field will not reflect the correct amount unless User also calls updateTotalBill,
    //which means Bandwidth class is now dependent on some other class to keep its internal state
    //consistent with the business logic.

}
