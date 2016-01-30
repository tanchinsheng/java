package q431.inheritance;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CleanConnector extends PortConnector {

    public CleanConnector(int port) throws IOException, FileNotFoundException, Exception {
        super(port);
    }

    public static void main(String[] args) {
    }
}
