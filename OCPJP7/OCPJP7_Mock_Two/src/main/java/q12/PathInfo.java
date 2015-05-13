/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q12;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class PathInfo {

    public static void main(String[] args) {
        Path aFilePath = Paths.get("D:\\dir\\file.txt");
        Iterator<Path> paths = aFilePath.iterator();
        while (paths.hasNext()) {
            System.out.println(paths.next() + " ");
        }

    }

}
