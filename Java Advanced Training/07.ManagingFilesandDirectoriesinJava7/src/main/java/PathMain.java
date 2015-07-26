
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathMain {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("temp/loremipsum.txt"); // need not exist
        System.out.println(path.toString());
        System.out.println(path.getFileName());
        System.out.println(path.getNameCount());
        System.out.println(path.getName(path.getNameCount() - 1));

        Path path2 = Paths.get("loremipsum.txt"); // need not exist
        System.out.println(path2.toString());
        System.out.println(path2.getNameCount());
        Path realPath = path2.toRealPath(LinkOption.NOFOLLOW_LINKS);
        System.out.println(realPath);
    }

}
