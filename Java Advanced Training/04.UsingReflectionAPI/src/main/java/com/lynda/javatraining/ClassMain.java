package com.lynda.javatraining;

import com.lynda.javatraining.olives.Olive;
import com.lynda.javatraining.olives.OliveColor;
import com.lynda.javatraining.olives.OliveName;

public class ClassMain {

    public static void main(String[] args) {
        Olive o = new Olive(OliveName.PICHOLINE, OliveColor.GREEN);
        Class<?> c = o.getClass();
        System.out.println(c);
        System.out.println(c.getName());
        System.out.println(c.getSimpleName());

    }

}
