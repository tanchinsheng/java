
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileFinderMain {

    public static void main(String[] args) throws IOException {

        Path fileDir = Paths.get("treefiles");
        FileFinder finder = new FileFinder("file3.txt");
        Files.walkFileTree(fileDir, finder);

        List<Path> foundFiles = finder.getFoundPaths();
        if (foundFiles.size() > 0) {
            for (Path foundFile : foundFiles) {
                System.out.println(foundFile.toRealPath(LinkOption.NOFOLLOW_LINKS));
            }
        } else {
            System.out.println("No files found!");
        }
    }
}
