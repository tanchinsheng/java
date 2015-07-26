
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoClient {

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 10007);
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                output.println(userInput);
                System.out.println("Echo: " + input.readLine());
            }
        } catch (UnknownHostException e) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, "Unknown host", e);
        } catch (IOException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, "Cannot connect to host", ex);
        }
    }
}
