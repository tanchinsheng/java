
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Whois {

    public static void main(String[] args) throws Exception {
        Socket sock = new Socket("whois.internic.net", 43);
        InputStream input = sock.getInputStream();
        OutputStream output = sock.getOutputStream();
        String strng = "infiniteskills.com\n";
        byte buffer[] = strng.getBytes();
        output.write(buffer);
        int ch;
        while ((ch = input.read()) != -1) {
            System.out.print((char) ch);
        }
        sock.close();
    }

}
