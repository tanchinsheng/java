package com.mycompany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Chapter4Examples {

    private static final int SIZE = 5;
    private static final int DIFFERENT_SIZE = 12;
    private static final int ROWS = 2;
    private static final int COLS = 3;

    public static void main(String[] args) {
        initialStuff();
        initializingArrayExamples();
        arrayOfObjectsExamples();
        multiDimensionalArrayExamples();

        // Comparing arrays
        int arr1[];
        int arr2[];
        arr1 = new int[5];
        arr2 = new int[5];

        for (int i = 0; i < 5; i++) {
            arr1[i] = 0;
            arr2[i] = 0;
        }

        // Element-by-element comparison
        boolean areEqual = true;

        int i;
        for (i = 0; i < 5; i++) {
            if (arr1[i] != arr2[i]) {
                areEqual = false;
            }
        }
        System.out.println(areEqual);

        // Using the equality operator
//        boolean areEqual = true;
        for (i = 0; i < 5; i++) {
            if (arr1[i] != arr2[i]) {
                areEqual = false;
            }
        }
        System.out.println(areEqual);

        // Using the equality operator
        //arr1 and arr2, reference different objects in memory
        // == operator just compare two pointers
        System.out.println("arr1 == arr2 is " + Boolean.toString(arr1 == arr2));
        // false

        // Indicates whether some other object is "equal to" this one.
        // equals inherits from Object
        System.out.println("arr1.equals(arr2) is " + arr1.equals(arr2));
        // false

        // Using the deepEquals method
        // Returns true if the two specified arrays of ints are equal to one another.
        //  two arrays are equal if they contain the same elements in the same order.
        System.out.println("Arrays.equals(arr1, arr2) is " + Arrays.equals(arr1, arr2));
        // true

        int grades[][] = new int[ROWS][COLS];

        grades[0][0] = 0;
        grades[0][1] = 1;
        grades[0][2] = 2;
        grades[1][0] = 3;
        grades[1][1] = 4;
        grades[1][2] = 5;

        int grades2[][];
        grades2 = new int[ROWS][];
        grades2[0] = new int[COLS];
        grades2[1] = new int[COLS];

        grades2[0][0] = 0;
        grades2[0][1] = 1;
        grades2[0][2] = 2;
        grades2[1][0] = 3;
        grades2[1][1] = 4;
        grades2[1][2] = 5;

        // Tests arrays reference variables and not the arrays
        System.out.println("grades == grades2 is " + Boolean.toString(grades == grades2));
        // false
        // Tests for object equivalent
        System.out.println("grades.equals(grades2) is " + grades.equals(grades2));
        // false
        // Performs a comparsion using object identities, this will work for one-dimensional arrays.
        System.out.println("Arrays.equals(grades, grades2) is " + Arrays.equals(grades, grades2));
        // false
        // Performs a more in depth examination of the elements for value equivalency using object's equals method
        // this method is appropriate for use with nested arrays of arbitrary depth
        System.out.println("Arrays.deepEquals(grades, grades2) is " + Arrays.deepEquals(grades, grades2));
        // true
        copyArraysExample();
        arraysClassExamples();
        arrayListExamples();
    }

    private static void initialStuff() {
        // One dimension arrays
        // int ages[];
        // ages = new int[5];

        int[] ages = new int[5];

        ages[0] = 35;
        System.out.println(ages[0]);

        int length = ages.length;
        System.out.println(length);

        // The placement of array brackets
//        ages = new [5]int;
//        [0]ages = 0;
    }

    private static void initializingArrayExamples() {
        // Initializing arrays
//        int ages[5] = {35, 10, 43, -5, 12};

//        int ages[] = {35, 10, 43, -5, 12};
//        System.out.println(ages.toString());
//
//        for (int i = 0; i < ages.length; i++) {
//            System.out.println(ages[i]);
//        }
        int ages[] = new int[SIZE];
        // initialize ages as needed

        for (int i = 0; i < ages.length; i++) {
            System.out.println(ages[i]);
        }

    }

    private static void arrayOfObjectsExamples() {
        String names[] = new String[5];
        names[2] = new String("Steve");

        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }
    }

    private static void multiDimensionalArrayExamples() {
        int grades[][] = new int[ROWS][COLS];

        grades[0][0] = 0;
        grades[0][1] = 1;
        grades[0][2] = 2;
        grades[1][0] = 3;
        grades[1][1] = 4;
        grades[1][2] = 5;

        for (int rows = 0; rows < ROWS; rows++) {
            for (int cols = 0; cols < COLS; cols++) {
                System.out.printf("%d  ", grades[rows][cols]);
            }
            System.out.println();
        }

        grades = new int[ROWS][];
        grades[0] = new int[COLS];
        grades[1] = new int[COLS];

        grades[0] = new int[4];
        grades[1] = new int[2];

    }

    private static void traversingArraysExamples() {

        int[] ages = new int[SIZE];

        // Using simple for loops
        for (int i = 0; i < ages.length; i++) {
            ages[i] = 5;
        }

//        int i=0;
//        while(i<ages.length) {
//            ages[i++] = 5;
//        }
        ages = new int[SIZE];

        for (int i = 0; i < SIZE; i++) {
            ages[i] = 5;
        }

        // Array redefined
        ages = new int[DIFFERENT_SIZE];
//			...
        for (int i = 0; i < SIZE; i++) {
            ages[i] = 5;
        }

        // Using the for each statement
        for (int number : ages) {
            number = 5;
        }
    }

    private static void arrayListExamples() {
        // default constructor is 10
        ArrayList list1 = new ArrayList();
        list1.add("item 1");
        list1.add("item 2");

        ArrayList list2 = new ArrayList(20);
        int arr1[] = new int[5];
        Arrays.fill(arr1, 5);

        String arr3[] = {"Pine", "Oak", "Maple", "Walnut"};
        System.out.println(Arrays.asList(arr3));
        arr3 = null;

        System.out.println(Arrays.toString(arr1));
        // an array can hold primitive data types or objects
        Object arr2[] = {"item 3", new Integer(5), list1};
        list1 = null;

        System.out.println("Arrays.asList(arr2)) is " + Arrays.asList(arr2));
        System.out.println("Arrays.toString(arr2)) is " + Arrays.toString(arr2));
        // intended to return a string representation of its array argument where the
        // array is more complex.
        System.out.println("Arrays.deepToString(arr2) is " + Arrays.deepToString(arr2));
        arr2 = null;

        ArrayList<String> creatures = new ArrayList<>();
        creatures.add("Mutant");
        creatures.add("Alien");
        creatures.add("Zombie");
        System.out.println(creatures);

        creatures.add(1, "Godzilla");
        System.out.println(creatures);

        ArrayList<String> cuddles = new ArrayList<>();
        cuddles.add("Tribbles");
        cuddles.add("Ewoks");

        creatures.addAll(2, cuddles);
        System.out.println(creatures);

        String element = creatures.get(2);
        System.out.println(element);
        System.out.println(creatures.indexOf("Tribbles"));
        System.out.println(creatures.indexOf("King Kong"));

        String[] complete = new String[0];
        complete = creatures.toArray(complete);
        for (String item : complete) {
            System.out.print(item + "- ");
        }
        System.out.println();

        for (String creature : creatures) {
            System.out.print(creature + " ");
        }
        System.out.println();

        for (int i = 0; i < creatures.size(); i++) {
            System.out.print(creatures.get(i) + " ");
        }
        System.out.println();

        Iterator<String> iterator = creatures.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();

        ListIterator<String> listIterator = creatures.listIterator();
        while (listIterator.hasNext()) {
            System.out.print(listIterator.next() + " ");
        }
        System.out.println();

        while (listIterator.hasPrevious()) {
            System.out.print(listIterator.previous() + " ");
        }
        System.out.println();
        System.out.println();

        System.out.println(creatures);
        creatures.remove(0);
        System.out.println(creatures);

        creatures.remove("Alien");
        System.out.println(creatures);

        creatures.removeAll(cuddles);
        System.out.println(creatures);

        creatures.clear();
        System.out.println(creatures);

//        creatures.set(0,"Ghoul");
//        System.out.println(creatures);
//
//        Collections.sort(creatures);
//        System.out.println(creatures);
    }

    private static void arraysClassExamples() {
        int arr1[] = new int[5];
        ArrayList list = new ArrayList();
        list.add("item 1");
        list.add("item 2");
        Object arr2[] = {"item 3", new Integer(5), list};
        String arr3[] = {"Pine", "Oak", "Maple", "Walnut"};

        Arrays.fill(arr1, 5);

        System.out.println(Arrays.asList(arr3));
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.deepToString(arr2));

        List list2 = Arrays.asList(arr3);
        arr3[0] = "Birch";
        System.out.println(Arrays.toString(arr3));

    }

    private static void copyArraysExample() {
        int arr1[] = new int[5];
        int arr2[] = new int[5];

        // Initialize arr1 elements to their index
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = i;
        }
        displayArray(arr1);

        // Simple array copy...tedious but can implement either a shallow or deep copy
        for (int i = 0; i < arr1.length; i++) {
            arr2[i] = arr1[i];
        }
        // Tests arrays reference variables and not the arrays...
        // arr1 and arr2 reference different objects in memory..
        // The contents of these two reference variables are different...
        // They dont' reference the same object
        // This only works properly if the two reference variables reference the same object.
        System.out.println("arr1 == arr2 is " + Boolean.toString(arr1 == arr2));
        // false
        // Tests for object equivalent and not object value equivalency
        // Object value equivalency refers to the condition where two
        // distinct objects are considered to be equivalent because their internal values are the same.
        // This only works properly if the two reference variables reference the same object.
        System.out.println("arr1.equals(arr2) is " + arr1.equals(arr2));
        // false
        // Performs a comparsion using object identities, this will work for one-dimensional arrays.
        System.out.println("Arrays.equals(arr1, arr2) is " + Arrays.equals(arr1, arr2));
        // true
        displayArray(arr2);

        // Initialize arr2 elements
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = 0;
        }
        displayArray(arr2);
        System.arraycopy(arr1, 0, arr2, 0, 5);
        displayArray(arr2);

        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = 0;
        }

        // shallow copy
        System.arraycopy(arr1, 0, arr2, 2, 3);
        displayArray(arr2);

        StringBuilder arr3[] = new StringBuilder[4];
        arr3[0] = new StringBuilder("Pine");
        arr3[1] = new StringBuilder("Oak");
        arr3[2] = new StringBuilder("Maple");
        arr3[3] = new StringBuilder("Walnut");

        StringBuilder arr4[] = new StringBuilder[4];
        // shallow copy...arr4 contains the same object reference variables used by arr3
        System.arraycopy(arr3, 0, arr4, 0, 4);
        System.out.println("== of new StringBuilder is " + Boolean.toString(arr3 == arr4));
        // false
        System.out.println("arr3.equals(arr4) is " + arr3.equals(arr4));
        // false
        System.out.println("Arrays.equals of new StringBuilder is " + Arrays.equals(arr3, arr4));
        // true
        System.out.println("Arrays.deepEquals of System.arraycopy(arr3, 0, arr4, 0, 4) is " + Arrays.deepEquals(arr3, arr4));
        // true

        String arr3a[] = new String[4];
        arr3a[0] = "Pine";
        arr3a[1] = "Oak";
        arr3a[2] = "Maple";
        arr3a[3] = "Walnut";

        String arr4a[] = new String[4];
        // shallow copy...arr4a contains the same object reference variables used by arr3a
        System.arraycopy(arr3a, 0, arr4a, 0, 4);
        System.out.println("== of new String is " + Boolean.toString(arr3a == arr4a));
        // false
        System.out.println("arr3a.equals(arr4a) is " + arr3a.equals(arr4a));
        // false
        System.out.println("Arrays.equals of new String is " + Arrays.equals(arr3a, arr4a));
        // true
        System.out.println("Arrays.deepEquals of System.arraycopy(arr3a, 0, arr4a, 0, 4) is " + Arrays.deepEquals(arr3a, arr4a));
        // true

        StringBuilder arr5[] = new StringBuilder[4];
        // deep copy...creation of an identical array with references to distinct strings
        for (int i = 0; i < arr3.length; i++) {
            arr5[i] = new StringBuilder(arr3[i]);
        }
        System.out.println("== of new StringBuilder is " + Boolean.toString(arr3 == arr5)); // false
        System.out.println("arr3.equals(arr5) is " + arr3.equals(arr5)); // false
        System.out.println("Arrays.equals of new StringBuilder is " + Arrays.equals(arr3, arr5)); // false
        System.out.println("Arrays.deepEquals of new StringBuilder is " + Arrays.deepEquals(arr3, arr5)); // false
        // false ?

//        System.arraycopy(arr1, 0, arr1, 3, 2);
//        displayArray(arr1);
        // deep copy...create a new array based on an existing array
        arr2 = Arrays.copyOf(arr1, 10);
        // 0 1 2 3 4 0 0 0 0 0
        displayArray(arr2);

        // deep copy...create a new array based on a sub-range of elements in an existing array
        arr2 = Arrays.copyOfRange(arr1, 3, 8);
        // 3 4 0 0 0
        displayArray(arr2);

        //shallow copy...both arrays will reference the same objects
        arr2 = arr1.clone();
        displayArray(arr2);

        System.out.println("Length of arr2: " + arr2.length);
        changeArray(arr2);
        System.out.println("Length of arr2: " + arr2.length);
    }

    // if we modify the arr parameter, the original arr2 variable is not modified.
    private static void changeArray(int arr[]) {
        arr = new int[100];
        System.out.println("Length of arr: " + arr.length);
    }

    // we are "passing a reference" to the arr2 array "by value"
    // we can read and write the elements of the arr2 array in the method.
    private static void displayArray(int arr[]) {
        for (int number : arr) {
            System.out.print(number + "  ");
        }
        System.out.println();
    }

    private static void zeroOutArray(int arr[]) {
        for (int number : arr) {
            number = 0;
        }
    }
}
