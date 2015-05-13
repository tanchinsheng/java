/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q09;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Javals {

    public static void main(String[] args) {
        Path currPath = Paths.get("src/main/java/q09");
        try (DirectoryStream<Path> javaFiles = Files.newDirectoryStream(currPath, "*.{java}")) {
            for (Path javaFile : javaFiles) {
                System.out.println(javaFile);
            }
        } catch (IOException ioe) {
            System.err.println("IO Error occurred");
            System.exit(-1);
        }
    }

}
