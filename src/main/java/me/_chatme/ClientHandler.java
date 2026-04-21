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
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        )
        {
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            Server.clinets.add(out);
            System.out.println("[" + clientAddr + "] Connected");
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
            System.out.print("Disconnect " + Server.clinets.size() + " Clients");
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
            client.println(message);
        }
    }
}
