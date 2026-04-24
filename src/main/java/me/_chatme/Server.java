package me._chatme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Server extends Application
{
    static List<PrintWriter> clinets = Collections.synchronizedList(new ArrayList<>());
    static List<String> clientAddresses = Collections.synchronizedList(new ArrayList<>());
    static int IDPT;

    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("ChatME Server");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error Loading Server FXML : " + e);
            e.printStackTrace();
        }
    }

    static void Start_Server() throws IOException
    {
        try (ServerSocket serverSocket = new ServerSocket(IDPT)) {
            javafx.application.Platform.runLater(() ->
            {
                if (Server_Controller.logAreaStatic != null) {
                    Server_Controller.logAreaStatic.appendText("Server is running and waiting for client connection...\n");
                }
            });
            while (true)
            {
                try {
                    // Accept incoming client connection
                    Socket clientSocket = serverSocket.accept();
                    javafx.application.Platform.runLater(() -> {
                        if (Server_Controller.logAreaStatic != null) {
                            Server_Controller.logAreaStatic.appendText("Client connected! " + clientSocket.getInetAddress() + "\n");
                        }
                    });
                    Thread t = new Thread(new ClientHandler(clientSocket));
                    t.start();
                } catch (IOException e) {
                    javafx.application.Platform.runLater(() -> {
                        if (Server_Controller.logAreaStatic != null) {
                            Server_Controller.logAreaStatic.appendText("Error Accepting connection " + e.getMessage() + "\n");
                        }
                    });
                }
            }

        }
    }

    public static void main(String args[]) throws IOException {
        launch(args);
    }
}