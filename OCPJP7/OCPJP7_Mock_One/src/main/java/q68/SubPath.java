/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q68;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SubPath {

    public static void main(String[] args) {
        Path aPath = Paths.get("C:\\WINDOWS\\system32\\config\\systemprofile\\Start Menu\\Programs\\Accessories\\Entertainment\\Windows Media Player");
        System.out.println(aPath.subpath(3, 4));
    }

}
