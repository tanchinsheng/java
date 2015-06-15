/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4.q07.my.school;

import chapter4.q07.my.city.Classroom;
import chapter4.q07.my.city.Classroom;

public class School {

    public static void main(String[] args) {
        System.out.println(Classroom.globalKey);
        Classroom room = new Classroom(101, "Mrs.Anderson");

        System.out.println(room.roomNumber);

        System.out.println(room.floor);

        System.out.println(room.teacherName);
    }
}
