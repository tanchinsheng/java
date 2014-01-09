package com.company.pretest;

import java.nio.file.*;
import java.util.Iterator;

public class PathInfo {

    public static void main(String[] args) {
        Path aFilePath = Paths.get("C:\\git\\OCPJP7\\Pretest\\file.txt"); // FILEPATH
//        while (aFilePath.iterator().hasNext()) {
//            System.out.println("path element: " + aFilePath.iterator().next());
//        }

        Iterator<Path> paths = aFilePath.iterator();
        while (paths.hasNext()) {
            System.out.println("path element: " + paths.next());
        }
    }
}