package ocpjp.pretest;

import java.nio.file.*;

class Relativize {

    public static void main(String[] args) {
        Path javaPath =
                Paths.get("C:\\git\\OCPJP7\\Pretest\\src\\main\\java\\com\\company\\pretest\\Relativize.java").normalize();
        Path classPath =
                Paths.get("C:\\git\\OCPJP7\\Pretest\\target\\classes\\com\\company\\pretest\\Relativize.class").normalize();
        System.out.println("javaPath" + javaPath);
        System.out.println("classPath" + classPath);
        Path result = javaPath.relativize(classPath);
        if (result == null) {
            System.out.println("relativize failed");
        } else if (result.equals(Paths.get(""))) {
            System.out.println("relative paths are same, so relativize returned empty path");
        } else {
            System.out.println(result);
        }
    }
}