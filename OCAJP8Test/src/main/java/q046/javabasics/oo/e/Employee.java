/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q046.javabasics.oo.e;

/**
 *
 * @author cstan
 */
class Person {

    //Insert code here
    protected int age;

    public int getAge() {
        return age;
    }

    protected void setAge(int age) {
        this.age = age;
    }

}

public class Employee extends Person {

    public static void main(String[] args) {
        Employee e = new Employee();
        e.setAge(29);
        System.out.println(e.getAge());
    }
}
