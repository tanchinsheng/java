package com.company.pretest;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class ArrayCompare 
{
    public static void main( String[] args )
    {
        int []arr1 = {1,2,3,4,5};
        int []arr2 = {1,2,3,4,5};
        // object similarity
        System.out.println( "arr1 == arr2 is " + (arr1 == arr2)); //false
        // compares this array object with the passed object array object, 
        // does not compare values of the array since it is inherited from the Object class.
        System.out.println( "arr1.equals(arr2) is " + (arr1.equals(arr2))); //false
        // Arrays class implements various equals() methods to compare array objects 
        // of different types
        // Two arrays are considered equal if both
        //arrays contain the same number of elements, and all corresponding pairs
        // of elements in the two arrays are equal.
        System.out.println( "Arrays.equals(arr1, arr2) is " + Arrays.equals(arr1, arr2)); // true
    }
}
