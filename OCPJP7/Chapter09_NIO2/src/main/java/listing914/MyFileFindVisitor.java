/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

class MyFileFindVisitor extends SimpleFileVisitor<Path> {

    private PathMatcher matcher;

    public MyFileFindVisitor(String pattern) {
        try {
            // FileSystems.getDefault().getPathMatcher("glob:*.{java,class}");
            matcher = FileSystems.getDefault().getPathMatcher(pattern);
        } catch (IllegalArgumentException iae) {
            System.err.println("Invalid pattern; did you forget to prefix \"glob:\"? (as in glob:*.java)");
            System.exit(-1);
        }

    }

    public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {
        find(path);
        return FileVisitResult.CONTINUE;
    }

    private void find(Path path) {
        Path name = path.getFileName();
        if (matcher.matches(name)) {
            System.out.println("Matching file:" + path.getFileName());
        }
        try {
            String type = Files.probeContentType(name);
            if (type == null) {
                System.err.format("'%s' has an" + " unknown filetype.%n", name);
            } else if (!type.equals("text/plain")) {
                System.err.format("'%s' is not" + " a plain text file.%n", name);
            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes) {
        find(path);
        return FileVisitResult.CONTINUE;
    }
}
