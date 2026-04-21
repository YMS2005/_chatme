package me._chatme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client_X extends Application
{
    public void start(Stage PrimaryStage)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
            Scene scene = new Scene(root);
            PrimaryStage.setTitle("ChatME");
            PrimaryStage.setScene(scene);
            PrimaryStage.show();
        }
        catch (IOException e)
        {
            System.err.println("Error Loading FXML : " + e);
            e.printStackTrace();
        }
    }
    public static void main(String args[]) throws IOException {

        launch(args);
        int ID;
        int IDPT;
        Scanner sc = new Scanner(System.in);

        System.out.print("Please enter ID: ");
        ID = sc.nextInt();

        System.out.print("Hi " + ID + " please enter your port number,\nit has to be the last 5 digits of your ID: ");
        IDPT = sc.nextInt();

        if (String.valueOf(Math.abs(IDPT)).length() < 5) {
            if (IDPT < 1024) {
                System.out.println("Reserved port, adding 10,000.");
                IDPT += 10000;
            } else {
                System.out.println("Less than 5 digits, exiting.");
                sc.close();
                return;
            }
        } else if (String.valueOf(Math.abs(IDPT)).length() > 5) {
            System.out.println("More than 5 digits, exiting.");
            sc.close();
            return;
        } else if (IDPT > 65535) {
            System.out.println("Port out of range, exiting.");
            sc.close();
            return;
        }

        System.out.println("Connecting as ID: " + ID + " on port: " + IDPT);

        try (
            Socket socket = new Socket("localhost", IDPT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner input = new Scanner(System.in)
        ) {
            System.out.println("Connected! Type messages (or 'quit' to exit):");

            // Thread: constantly listens for incoming broadcasts
            Thread listener = new Thread(() -> {
                try {
                    String incoming;
                    while ((incoming = in.readLine()) != null) {
                        System.out.println(incoming);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            listener.setDaemon(true);
            listener.start();

            // Main thread: handles sending
            while (true) {
                String msg = input.nextLine();
                if (msg.equalsIgnoreCase("quit")) break;
                out.println("[" + ID + "]: " + msg);
            }

        } catch (ConnectException e) {
            System.out.println("Could not connect. Is the server running?");
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }

        System.out.println("Disconnected.");
        sc.close();
    }
}