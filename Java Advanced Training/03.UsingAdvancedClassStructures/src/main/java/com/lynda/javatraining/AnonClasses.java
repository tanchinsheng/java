package com.lynda.javatraining;

import com.lynda.olivepress.olives.AnonOliveJar;

public class AnonClasses {

    public static void main(String[] args) throws Exception {

        AnonOliveJar jar = new AnonOliveJar();
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.addOlive("Kalamata", 0x000000);
        jar.reportOlives();
    }

}
