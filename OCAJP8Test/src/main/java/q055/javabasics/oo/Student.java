/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q055.javabasics.oo;

import java.util.ArrayList;

/**
 *
 * An important aspect of encapsulation is that other classes should not be able
 * to modify the state fields of a class directly. Therefore, the data members
 * should be private (or protected if you want to allow subclasses to inherit
 * the field) and if the class wants to allow access to these fields, it should
 * provide appropriate setters and getters with public access. If you return the
 * same scores list, the caller would be able to add or remove elements from it,
 * thereby rendering the average incorrect. This can be prevented by returning a
 * copy of the list.
 */
public class Student {

    // ArrayList<Integer> scores;
    private ArrayList<Integer> scores;
    private double average;

//    public ArrayList<Integer> getScores() {
//        return scores;
//    }
    public ArrayList<Integer> getScores() {
        return new ArrayList(scores);
    }

    public double getAverage() {
        return average;
    }

    private void computeAverage() {
        //valid code to compute average
        average =//update average value
    }

    public Student() {
        computeAverage();
    }
}
