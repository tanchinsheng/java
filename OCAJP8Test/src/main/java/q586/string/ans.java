/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q586.string;

/**
 *
 * Which of these are valid expressions to create a string of value "hello
 * world" ?
 */
public class ans {

    /**
     * All the expressions are legal. String literals are String objects and can
     * be used just like any other object.
     */
    public static void main(String[] args) {
        System.out.println(" hello world".trim()); //hello world
        //trim() removes starting and ending spaces.

        System.out.println("hello" + new String("world")); //helloworld
        //It will create helloworld. No space between hello and world.

        System.out.println("hello".concat(" world")); //hello world

        System.out.println(new StringBuilder("world").insert(0, "hello ").toString());//hello world

        System.out.println(new StringBuilder("world").append("hello ", 0, 6).toString());//worldhello
        //There is an append method that takes two ints as shown here but the int parameters are to determine the portion
        //of the String that is to be appended to the target. That portion will still be appended to the end of the target.

        System.out.println(new StringBuilder("world").append(0, "hello ").toString());
        //1. append adds the argument to the end.
        //2. It doesn't take an int parameter.

        System.out.println(new StringBuilder("world").add(0, "hello ").toString());
        //There is no add method in StringBuilder.
    }

}
