/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q16;

interface EnumBase {

}

enum AnEnum implements EnumBase {

    ONLY_MEM; // implicitly public, static and final
}

public class EnumCheck {

    public static void main(String[] args) {
        if (AnEnum.ONLY_MEM instanceof AnEnum) {
            System.out.println("yes, instance of AnEnum");
        }
        if (AnEnum.ONLY_MEM instanceof EnumBase) {
            System.out.println("yes, instance of EnumBase");
        }
        if (AnEnum.ONLY_MEM instanceof Enum) {
            System.out.println("yes, instance of Enum");
        }
    }

}
