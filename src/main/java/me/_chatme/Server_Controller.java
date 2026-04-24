package me._chatme;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Server_Controller implements Initializable {

    @FXML private TextField portField;
    @FXML private Button startButton;
    @FXML private ListView<String> clientList;

    // Kept as null — ClientHandler already null-checks it before using
    public static TextArea logAreaStatic = null;
    public static ListView<String> clientListStatic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientListStatic = clientList;
        startButton.setOnAction(e -> startServer());
    }

    @FXML
    private void startServer() {
        try {
            int port = Integer.parseInt(portField.getText().trim());
            if (String.valueOf(Math.abs(port)).length() < 5) {
                if (port < 1024) {
                    port += 10000;
                } else {
                    System.out.println("Invalid port: must be 5 digits.");
                    return;
                }
            } else if (port > 65535) {
                System.out.println("Port out of range.");
                return;
            }
            Server.IDPT = port;
            System.out.println("Starting server on port " + port);
            Thread serverThread = new Thread(() -> {
                try {
                    Server.Start_Server();
                } catch (Exception ex) {
                    System.out.println("Server error: " + ex.getMessage());
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number.");
        }
    }
}