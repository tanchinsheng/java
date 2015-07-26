
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(10007);
                Socket clientSocket = serverSocket.accept();
                PrintWriter serverOutput = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Listening for connection...");
            System.out.println("Connection successful.");
            System.out.println("Listening for input...");
            String inputLine;
            while ((inputLine = serverInput.readLine()) != null) {
                System.out.println("Server: " + inputLine);
                serverOutput.println(inputLine);
                if (inputLine.equals("bye")) {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, "Can't listen on 10007 or Accept failed.", ex);
            System.exit(1);
        }
    }
}
