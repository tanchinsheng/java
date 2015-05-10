/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q69;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

    public static void main(String[] args) {
        Path testFilePath = Paths.get("C:\\WINDOWS\\system32\\config\\.\\systemprofile\\Start Menu\\Programs\\Accessories\\Entertainment\\..\\..");
        System.out.println(testFilePath.normalize().toAbsolutePath());
    }

}
