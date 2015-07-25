
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient {

    public static void main(String[] args) {

        try (Socket clientSocket = new Socket("127.0.0.1", 10007);
                BufferedReader ServerInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintStream clientOutput = new PrintStream(clientSocket.getOutputStream());
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (true) {
                System.out.print("Client: ");
                line = consoleInput.readLine();
                clientOutput.println(line);
                line = ServerInput.readLine();
                System.out.print("Server: " + line + "\n");
                if (line.equals("bye")) {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
