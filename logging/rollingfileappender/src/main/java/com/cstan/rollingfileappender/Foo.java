package com.cstan.rollingfileappender;

import org.apache.log4j.Logger;

public class Foo {

    private static final Logger logger = Logger.getLogger(Foo.class);

    public static void main(String[] args) {
        for (int x = 0; x < 100000; x++) {
            logger.error("goodness this shouldn't be happening to us right here!!!!" + x);
        }
    }
}
