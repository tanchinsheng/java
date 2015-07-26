
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTreeMain {

    public static void main(String[] args) throws IOException {

        Path fileDir = Paths.get("treefiles");
        MyFileVisitor visitor = new MyFileVisitor();
        Files.walkFileTree(fileDir, visitor);
    }
}
