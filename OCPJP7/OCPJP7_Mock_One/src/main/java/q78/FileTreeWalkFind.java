/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q78;

import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

class MyFileFindVisitor extends SimpleFileVisitor<Path> {

    private final PathMatcher matcher;

    public MyFileFindVisitor(String pattern) {
        matcher = FileSystems.getDefault().getPathMatcher(pattern);
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {
        find(path);
        return FileVisitResult.CONTINUE;
    }

    private void find(Path path) {
        Path name = path.getFileName();
        if (matcher.matches(name)) {
            System.out.println("Matching file: " + path.getFileName());
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes) {
        find(path);
        return FileVisitResult.CONTINUE;
    }
}

class FileTreeWalkFind {

    public static void main(String[] args) {
        Path startPath = Paths.get(".\\src\\main\\java\\q78");
        String pattern = "glob:File[0-9]+.java"; // glob does not support +
        //String pattern = "glob:File[0-9]*.java"; // glob support *
        try {
            Files.walkFileTree(startPath, new MyFileFindVisitor(pattern));
            System.out.println("File Search completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
