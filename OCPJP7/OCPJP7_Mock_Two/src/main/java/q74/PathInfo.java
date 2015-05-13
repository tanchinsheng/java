/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q74;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathInfo {

    public static void main(String[] args) {
        Path testFilePath = Paths.get(".\\Test");
        System.out.println("file name: " + testFilePath.getFileName());
        System.out.println("absolute path: " + testFilePath.toAbsolutePath());
        System.out.println("Normalized path: " + testFilePath.normalize());
    }

}
