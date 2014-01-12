/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstraction;

/**
 *
 * @author cstan
 */
/* File name : Employee.java */

/*The class must also be declared abstract. 
 * If a class contains an abstract method, the class must be abstract as well.*/
public abstract class Employee {

    private String name;
    private String address;
    private int number;

    public Employee(String name, String address, int number) {
        System.out.println("Constructing an Employee");
        this.name = name;
        this.address = address;
        this.number = number;
    }

    /*Abstract method would have no definition, and its signature is followed by a semicolon */
    public abstract String showString();
    
    public double computePay() {
        System.out.println("Inside Employee computePay");
        return 0.0;
    }

    public void mailCheck() {
        System.out.println("Mailing a check to " + this.name
                + " " + this.address);
    }

    public String toString() {
        return name + " " + address + " " + number;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String newAddress) {
        address = newAddress;
    }

    public int getNumber() {
        return number;
    }
}
