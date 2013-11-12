/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part23interfaces;

public class Machine implements IInfo {

    private final int id = 7;

    public void start() {
        System.out.println("Machine started.");
    }
    @Override
    public void showInfo() {
        System.out.println("Machine ID is: " + id);
    }
}
