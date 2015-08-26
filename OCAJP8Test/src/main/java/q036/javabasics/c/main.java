/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q036.javabasics.c;

/**
 *
 * A public class must exist in a file by the same name. So this code is invalid
 * because anotherone is a public class but the name of the file is main. It
 * would have been valid if the name of the file were anotherone.java.
 *
 * A non public class may exist in any file. This implies that there can be only
 * one public class in a file.
 */
public class anotherone {
}

class main {

    public static void main(String[] args) {
        System.out.println("hello");
    }
}
