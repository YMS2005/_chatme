package me._chatme;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable
{
    private final Socket clientSocket;
    private PrintWriter out;


    public ClientHandler(Socket socket)
    {
        this.clientSocket = socket;
    }

    @Override
    public void run()
    {
        String clientAddr = clientSocket.getInetAddress().toString();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
        {
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            Server.clinets.add(out);
            Server.clientAddresses.add(clientAddr);
            System.out.println("[" + clientAddr + "] Connected");

            broadcast("CLIENTS:" + String.join(",", Server.clientAddresses));

            javafx.application.Platform.runLater(() -> {
                if (Server_Controller.logAreaStatic != null) {
                    Server_Controller.logAreaStatic.appendText("[" + clientAddr + "] Connected\n");
                }
                if (Server_Controller.clientListStatic != null) {
                    Server_Controller.clientListStatic.getItems().add(clientAddr);
                }
            });

            String Message;
            while ((Message = in.readLine()) != null)
            {
                System.out.println(Message);
                broadcast(Message);
                // out.println("Server received : " + Message);    
            }
        }
        catch (Exception e) 
        {
            System.out.println("[" + clientAddr + "] Disconnected " + e.getMessage());
        }
        finally
        {
            Server.clinets.remove(out);
            Server.clientAddresses.remove(clientAddr);
            broadcast("CLIENTS:" + String.join(",", Server.clientAddresses));
            System.out.print("Disconnect " + Server.clinets.size() + " Clients");
            javafx.application.Platform.runLater(() -> {
                if (Server_Controller.logAreaStatic != null) {
                    Server_Controller.logAreaStatic.appendText("[" + clientAddr + "] Disconnected\n");
                }
                if (Server_Controller.clientListStatic != null) {
                    Server_Controller.clientListStatic.getItems().remove(clientAddr);
                }
            });
            try
            {
                clientSocket.close();
            } 
            catch (Exception e) 
            {
                System.out.println("Error closing socket : " + e.getMessage());
            }
        }
        System.out.println("[" + clientAddr + "] Connection Closed");
    }

    private void broadcast(String message) {
        for (PrintWriter client : Server.clinets) {
            if (client != out) {
                client.println(message);
            }
        }
    }
}
