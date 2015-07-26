package com.lynda.javatraining;

import com.lynda.olivepress.olives.EnumOliveJar;
import com.lynda.olivepress.olives.EnumOliveName;

public class EnumClasses {

    public static void main(String[] args) throws Exception {

        EnumOliveJar jar = new EnumOliveJar();
        jar.addOlive(EnumOliveName.KALAMATA, 0x000000);
        jar.addOlive(EnumOliveName.PICHOLINE, 0x000000);
        jar.addOlive(EnumOliveName.LIGURIO, 0x000000);
        jar.reportOlives();
    }

}
