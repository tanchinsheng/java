/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q45;

import java.util.HashSet;

class Student {

    public Student(int r) {
        rollNo = r;
    }
    int rollNo;
}

public class Test {

    public static void main(String[] args) {
        HashSet<Student> students = new HashSet<>();
        students.add(new Student(5));
        students.add(new Student(10));
        System.out.println(students.contains(new Student(10)));
    }

}
