package com.lynda.javatraining;

import com.lynda.olivepress.olives.InstanceOliveJar;
import com.lynda.olivepress.olives.Olive;
import java.util.List;

public class InstanceInit {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting application...");
        List<Olive> olives = new InstanceOliveJar(3, "Kalamata", 0x000000).olives;
        for (Olive o : olives) {
            System.out.println("It's a " + o.getOliveName() + " olive!");
        }

    }

}
