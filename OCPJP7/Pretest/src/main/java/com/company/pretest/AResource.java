package com.company.pretest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

class AResource implements AutoCloseable {

    // In Closeable, it is declared as public void close() throws IOException
    public void close() throws IOException {
// body of close to release the resource
    }

    public static void main(String[] s) throws FileNotFoundException {

    
    }
}
