package listing908;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

class FileAttributes2 {

    public static void main(String[] args) {
        Path path = Paths.get(args[0]);
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
            System.out.println("File size: " + fileAttributes.size());
            System.out.println("isDirectory: " + fileAttributes.isDirectory());
            System.out.println("isRegularFile: " + fileAttributes.isRegularFile());
            System.out.println("isSymbolicLink: " + fileAttributes.isSymbolicLink());
            System.out.println("File last accessed time: " + fileAttributes.lastAccessTime());
            System.out.println("File modified time: " + fileAttributes.lastModifiedTime());
            System.out.println("File creation time: " + fileAttributes.creationTime());

            long currentTime = System.currentTimeMillis();
            FileTime ft = FileTime.fromMillis(currentTime);
            Files.setLastModifiedTime(path, ft);
            System.out.println("File last modified time: "
                    + Files.getAttribute(path, "lastModifiedTime", LinkOption.NOFOLLOW_LINKS));

            FileStore store = Files.getFileStore(path);

            long total = store.getTotalSpace() / 1024;
            long used = (store.getTotalSpace()
                    - store.getUnallocatedSpace()) / 1024;
            long avail = store.getUsableSpace() / 1024;

            System.out.format("total = %d, used = %d, avail = %d%n", total, used, avail);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
