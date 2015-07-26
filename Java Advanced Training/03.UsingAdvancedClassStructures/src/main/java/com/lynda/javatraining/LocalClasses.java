package com.lynda.javatraining;

import com.lynda.olivepress.olives.LocalOliveJar;

public class LocalClasses {

    public static void main(String[] args) throws Exception {

        LocalOliveJar jar = new LocalOliveJar();
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.reportOlives();
    }

}
