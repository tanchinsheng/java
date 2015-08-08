package Debugger;

import java.net.URI;
import java.net.URISyntaxException;

public class App {

    public static void main(String[] args) {
        try {
            URI uri = new URI("http:\\somecompany.com");
        } catch (URISyntaxException ex) {
            // ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        System.out.println("Running");

    }

}
