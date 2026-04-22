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

    @FXML
    private TextField portField;
    @FXML
    private Button startButton;
    @FXML
    private TextArea logArea;
    @FXML
    private ListView<String> clientList;

    public static TextArea logAreaStatic;
    public static ListView<String> clientListStatic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logAreaStatic = logArea;
        clientListStatic = clientList;
        startButton.setOnAction(e -> startServer());
    }

    @FXML
    private void startServer() {
        try {
            int port = Integer.parseInt(portField.getText().trim());
            if (String.valueOf(Math.abs(port)).length() < 5) {
                if (port < 1024) {
                    logArea.appendText("Adjusting port to " + (port + 10000) + "\n");
                    port += 10000;
                } else {
                    logArea.appendText("Invalid port: must be last 5 digits of ID.\n");
                    return;
                }
            } else if (port > 65535) {
                logArea.appendText("Port out of range.\n");
                return;
            }
            Server.IDPT = port;
            logArea.appendText("Starting server on port " + port + "\n");
            Thread serverThread = new Thread(() -> {
                try {
                    Server.Start_Server();
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> logArea.appendText("Server error: " + ex.getMessage() + "\n"));
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();
        } catch (NumberFormatException e) {
            logArea.appendText("Invalid port number.\n");
        }
    }
}
