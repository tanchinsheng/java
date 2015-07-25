
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(10007);
                Socket clientSocket = serverSocket.accept();
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintStream serverOutput = new PrintStream(clientSocket.getOutputStream());
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (true) {
                line = clientInput.readLine();
                if (line.equals("bye")) {
                    break;
                }
                System.out.println("Client: " + line);
                System.out.print("Server: ");
                line = consoleInput.readLine();
                serverOutput.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
