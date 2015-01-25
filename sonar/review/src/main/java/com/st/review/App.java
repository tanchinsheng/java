package com.st.review;

import java.io.InputStream;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        String urlStr = "http://xeo.com:90/register.jsp?name=joe";
        try {
            URL url = new URL(urlStr);
            InputStream is = url.openStream();
            is.close();
        } catch (Exception e) {
            // Print out the exception that occurred
            //System.out.println(urlStr + ": " + e.getMessage());
        }
    }
}
