/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packt;

/**
 *
 * @author cstan
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int number = 10;
        Employee employee = new Employee();
        employee.setAge(11);
        changeValues(number, employee);
        System.out.println(number);
        System.out.println(employee.getAge());
    }

    private static void changeValues(int num, Employee employee) {
        num = 20;
        employee.setAge(22);
        employee = new Employee();
        employee.setAge(33);
    }

}
