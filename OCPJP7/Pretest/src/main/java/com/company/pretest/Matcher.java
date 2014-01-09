package com.company.pretest;

import java.io.IOException;
import java.nio.file.*;

class Matcher {

    public static void main(String[] args) {
        Path currPath = Paths.get(".");
        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(currPath, "*o*?{java,class}")) {
            for (Path file : stream) {
                System.out.print(file.getFileName() + " ");
            }
        } catch (IOException ioe) {
            System.err.println("An I/O error occurred... exiting ");
        }
    }
}