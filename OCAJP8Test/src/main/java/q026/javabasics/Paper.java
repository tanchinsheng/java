/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q026.javabasics;

/**
 *
 * @author cstan
 */
public class Paper {

    /**
     * You may find a few simple questions in the exam that expect you to know
     * what is printed when you pass an array to System.out.print/println. All
     * you need to know is that when the class (or the superclass) of an object
     * does not override the toString method, Object class's toString is used,
     * which prints the name of the class + @ sign + hash code of the object.
     * Now, in case of an array, the name of the class is a little complicated.
     * The details (given here:
     * http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#getName-- )
     * are: The internal form of the name consists of the name of the element
     * type preceded by one or more '[' characters representing the depth of the
     * array nesting. The encoding of element type names is as follows: Element
     * Type   Encoding boolean   Z byte      B char      C class or
     * interface   Lclassname;  <-- Observe the character L here double   D
     * float       F int          I long       J short     S For example, the
     * name of one dimensional byte array is [B. Therefore, if you pass it to
     * print/println method, [B@<hashcode> will be printed. The name of two
     * dimensional byte array is [[B. Therefore, if you pass it to print/println
     * method, [[B@<hashcode> will be printed. Thus, in this question, the first
     * println statement will print [LPaper@<hashcode> and the second println
     * will print Paper@<hashcode>, both followed by a new line, of course.
     */
    public String title;
    public int id;

    public Paper(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public static void main(String[] args) {
        Paper[] papers = {
            new Paper("T1", 1), new Paper("T2", 2), new Paper("T3", 3)
        };
        System.out.println(papers);
        System.out.println(papers[1]);
        System.out.println(papers[1].id);
    }

}
