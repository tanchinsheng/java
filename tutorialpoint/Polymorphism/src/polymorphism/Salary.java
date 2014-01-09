/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polymorphism;

/**
 *
 * @author cstan
 */
/* File name : Salary.java */
public class Salary extends Employee {

    private double salary; //Annual salary

    public Salary(String name, String address, int number, double salary) {
        super(name, address, number);
        setSalary(salary);
    }

    /*While invoking s.mailCheck() the compiler sees mailCheck() in the Salary class at compile time, 
     * and the JVM invokes mailCheck() in the Salary class at run time.*/
    public void mailCheck() {
        System.out.println("Within mailCheck of Salary class ");
        System.out.println("Salary Class: Mailing check to " + getName()
                + " with salary " + salary);
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double newSalary) {
        if (newSalary >= 0.0) {
            salary = newSalary;
        }
    }

    public double computePay() {
        System.out.println("Computing salary pay for " + getName());
        return salary / 52;
    }
}