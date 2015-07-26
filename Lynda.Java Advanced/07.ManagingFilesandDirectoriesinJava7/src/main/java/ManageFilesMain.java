
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ManageFilesMain {

    public static void main(String[] args) throws IOException {

        Path source = Paths.get("files/loremipsum.txt");
        System.out.println(source.getFileName());
        Path target = Paths.get("files/newfile.txt");
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

        Path tocreate = Paths.get("files/tocreate.txt");
        Files.deleteIfExists(tocreate);
        Files.createFile(tocreate);
        System.out.println("Created file!");

        Path toDelete = Paths.get("files/tocreate.txt");
        Files.deleteIfExists(toDelete);
        System.out.println("File Delete!");

        Path tomove = Paths.get("files/newdir/tomove.txt");
        Files.deleteIfExists(tomove);

        Path newdir = Paths.get("files/newdir");
        Files.deleteIfExists(newdir);

        Files.createDirectory(newdir);

        Files.createFile(tomove);
        Files.move(tomove, newdir.resolve(tomove.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File moved!");
    }

}
