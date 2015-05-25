package listing913;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.io.IOException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

// Our File visitor implementation that performs copy
class MyFileCopyVisitor extends SimpleFileVisitor<Path> {

    private Path source, destination;

    public MyFileCopyVisitor(Path s, Path d) {
        source = s;
        destination = d;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {
        Path newd = destination.resolve(source.relativize(path));
        System.out.println("visitFile...path:" + path + ", newd:" + newd);
        try {
            Files.copy(path, newd, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes) {
        Path newd = destination.resolve(source.relativize(path));
        System.out.println("preVisitDirectory...path:" + path + ", newd:" + newd);
        try {
            Files.copy(path, newd, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileVisitResult.CONTINUE;
    }

    //This case can occur only when the program is following symbolic links
    @Override
    public FileVisitResult
            visitFileFailed(Path file, IOException exc) {
        if (exc instanceof FileSystemLoopException) {
            System.err.println("cycle detected: " + file);
        } else {
            System.err.format("Unable to copy:" + " %s: %s%n", file, exc);
        }
        return FileVisitResult.CONTINUE;
    }
}
