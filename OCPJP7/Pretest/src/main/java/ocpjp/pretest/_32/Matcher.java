package ocpjp.pretest._32;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Matcher {

    public static void main(String[] args) {

        Path currPath = Paths.get(".\\src\\main\\java\\ocpjp\\pretest\\_32");
        try (DirectoryStream<Path> stream
                = Files.newDirectoryStream(currPath, "*o*?{java,class}")) {
            for (Path file : stream) {
                System.out.print(file.getFileName() + " ");
            }
        } catch (IOException ioe) {
            System.err.println("An I/O error occurred... exiting ");
        }
    }
}
