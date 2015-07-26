package com.lynda.javatraining;

import com.lynda.javatraining.exceptions.WrongFileException;
import com.lynda.javatraining.util.MyFileReader;
import java.io.IOException;

public class ThrowMain {

    public static void main(String[] args) throws IOException {
        //String fileContents = MyFileReader.readFile("textFile1.txt");
        String fileContents = MyFileReader.readFile("textFile2.txt");
        System.out.println(fileContents);

        try {
            if (fileContents.equals("Right file")) {
                System.out.println("You chose the right file!");
            } else {
                throw (new WrongFileException());
            }
        } catch (WrongFileException wrongFileException) {
            System.out.println(wrongFileException.getMessage());
        }

    }

}
