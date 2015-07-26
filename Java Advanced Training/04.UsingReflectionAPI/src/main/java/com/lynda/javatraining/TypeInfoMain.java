package com.lynda.javatraining;

import com.lynda.javatraining.olives.Olive;
import com.lynda.javatraining.olives.OliveColor;
import com.lynda.javatraining.olives.OliveName;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TypeInfoMain {

    public static void main(String[] args) {
        Olive o = new Olive(OliveName.PICHOLINE, OliveColor.GREEN);

        Class<?> c = o.getClass();

        System.out.println(c);
        System.out.println(c.getName());
        System.out.println(c.getSimpleName());

        Constructor<?>[] constructors = c.getConstructors();
        System.out.println("Number of constructors: " + constructors.length);
        Constructor<?> con = constructors[0];

        Object obj = null;
        try {
            obj = con.newInstance(OliveName.PICHOLINE, OliveColor.GREEN);
            System.out.println(obj);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(TypeInfoMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
