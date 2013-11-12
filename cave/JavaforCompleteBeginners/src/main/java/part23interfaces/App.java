/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part23interfaces;

/**
 * A tutorial on interfaces in Java. Discover how you can use interfaces almost
 * everywhere where you’d normally use classes, with one important exception.
 * Interfaces have a variety of uses, including helping to specify
 * functionality, helping to separate one class from another, summarising
 * functionality among similar classes and implementing the Java version of a
 * “callback”. Although I don’t cover all possible uses here, in this video I
 * show you almost all of the mechanics of creating and using interfaces.
 */
public class App {

    public static void main(String[] args) {

        Machine mach1 = new Machine();
        mach1.start();

        Person person1 = new Person("Bob");
        person1.greet();

        IInfo info1 = new Machine();
        info1.showInfo();

        IInfo info2 = person1;
        info2.showInfo();

        System.out.println();

        outputInfo(mach1);
        outputInfo(person1);
    }

    private static void outputInfo(IInfo info) {
        info.showInfo();
    }

}
