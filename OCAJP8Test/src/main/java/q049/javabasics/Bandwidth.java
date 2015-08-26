/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q049.javabasics;

/**
 *
 * @author cstan
 */
public class Bandwidth {

    private int totalUsage;
    private double totalBill;
    private double costPerByte;

    //add your code here
    public void addUsage(int bytesUsed) {
        if (bytesUsed > 0) {
            totalUsage = totalUsage + bytesUsed;
            totalBill = totalBill + bytesUsed * costPerByte;
        }
    }
}
