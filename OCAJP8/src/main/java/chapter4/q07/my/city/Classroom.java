/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4.q07.my.city;

public class Classroom {

    private int roomNumber;
    protected String teacherName;
    static int globalKey = 54321;
    public int floor = 3;

    Classroom(int r, String t) {
        roomNumber = r;
        teacherName = t;
    }
}
