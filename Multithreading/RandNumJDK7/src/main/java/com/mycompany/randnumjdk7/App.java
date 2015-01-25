package com.mycompany.randnumjdk7;

import java.util.concurrent.ThreadLocalRandom;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //available only in jdk 7
        int r = ThreadLocalRandom.current() .nextInt(4, 77);
        System.out.println( "Random number :" + r); 
    }
}
