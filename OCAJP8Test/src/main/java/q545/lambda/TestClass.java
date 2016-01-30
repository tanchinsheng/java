/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q545.lambda;

/**
 *
 * @author cstan
 */
import java.util.function.Predicate;

class Employee {

    int age; //1
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static boolean validateEmployee(Employee e, Predicate<Employee> p) {
        return p.test(e);
    }

    public static void main(String[] args) {
        Employee e = new Employee(); //2        
        System.out.println(validateEmployee(e, e -> e.age < 10000)); //3
    }

}
