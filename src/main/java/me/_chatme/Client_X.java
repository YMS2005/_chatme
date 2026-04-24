package me._chatme;

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

public class Client_X extends Login_Controller
{
    public static java.net.Socket socket;
    public static java.io.PrintWriter out;

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

    public static void main(String args[]) throws IOException
    {
        Login_Controller LG = new Login_Controller();
        launch(args);
        System.out.println("Connecting as ID: " + Login_Controller.ID + " on port: " + Login_Controller.IDPT);
        startConnection();
        System.out.println("Disconnected.");
    }

    public static void startConnection() {
        try {
            socket = new Socket("localhost", Login_Controller.IDPT);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected! You can now send messages from the GUI.");


            // Thread: constantly listens for incoming broadcasts
            Thread listener = new Thread(() -> {
                try {
                    String incoming;
                    while ((incoming = in.readLine()) != null) {
                        String finalIncoming = incoming;
                        javafx.application.Platform.runLater(() -> {
                            if (finalIncoming.startsWith("CLIENTS:")) {
                                String[] clients = finalIncoming.substring(8).split(",");
                                if (Main_Controller.staticClientList != null) {
                                    Main_Controller.staticClientList.getItems().setAll(clients);
                                }
                            } else {
                                if (Main_Controller.staticChatArea != null) {
                                    Main_Controller.staticChatArea.appendText(finalIncoming + "\n");
                                }
                            }
                        });
                        System.out.println(incoming);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            listener.setDaemon(true);
            listener.start();
        } catch (ConnectException e) {
            System.out.println("Could not connect. Is the server running?");
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}