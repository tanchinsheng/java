package com.lynda.javatraining;

import com.lynda.olivepress.olives.MemberOliveJar;

public class MemberClasses {

    public static void main(String[] args) throws Exception {

        MemberOliveJar jar = new MemberOliveJar();
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.reportOlives();
    }

}
