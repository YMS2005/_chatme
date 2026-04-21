package me._chatme;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Server
{
    static List<PrintWriter> clinets = Collections.synchronizedList(new ArrayList<>());
public static void main(String args[]) throws IOException 
    {
        int IDPT; // port number
        Scanner sc = new Scanner(System.in); //Scanner to read data
        System.out.print("please enter you port number,\n it has to be the last 5 digits of your ID:");
        IDPT = sc.nextInt(); // Read User Port
            if (String.valueOf(Math.abs(IDPT)).length() < 5) /*making sure the user enter the last 5 digits of his ID.
            if not, we check if the port number is less than 1024 since its 4 digits*/
            {
                if (IDPT < 1024) // 1 - 1023 standred ports 
                {
                    System.out.println("uh oh, your ID is in the range of Standered Ports that are not for custom use, \n don't worry I got you,I'll just add 10,000 to your number");
                    IDPT += 1000;
                    sc.close();    
                }
                else //if it is not less than 1024 than it can't work since the port number is most likely from 1 - 1023 which cannot be used by the user
                {
                    System.out.println("uh oh you enter more than 5 digits make sure you enter the right number before procedding.");
                    sc.close();
                    return;
                }
            }
            else if(IDPT > 65535) //port limit
            {
                System.out.println("uh oh your port is out of range, make sure you enter the last 4 digits of your ID instead.");
                sc.close();
                return;                  
            }

        try(ServerSocket serverSocket = new ServerSocket(IDPT))
        {
            System.out.println("Server is running and waiting for client connection...");
            while (true) 
            {
                try
                {
                    // Accept incoming client connection
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected! " + clientSocket.getInetAddress());        
                    Thread t = new Thread(new ClientHandler(clientSocket));
                    t.start();
                }
                catch(IOException e)
                {
                    System.out.println("Error Accepting connection " + e.getMessage());
                }
            }

        }
    }
}
