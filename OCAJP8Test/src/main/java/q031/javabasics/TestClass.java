/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q031.javabasics;

/**
 *
 * The following are the complete contents of TestClass.java file. Which
 * packages are automatically imported? If there is no package statement in the
 * source file, the class is assumed to be created in a default package that has
 * no name. In this case, all the types created in this default package will be
 * available to this class without any import statement. However, note that this
 * default package cannot be imported in classes that belong to any other
 * package at all, not even with any sort of import statement. So for example,
 * if you have a class named SomeClass in package test, you cannot access
 * TestClass defined in the problem statement (as it is defined in the default
 * package) at all because there is no way to import it. As per JLS Section 7.5:
 * A type in an unnamed package has no canonical name, so the requirement for a
 * canonical name in every kind of import declaration implies that (a) types in
 * an unnamed package cannot be imported, and (b) static members of types in an
 * unnamed package cannot be imported.
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("hello");
    }

}
