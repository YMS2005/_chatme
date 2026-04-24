package me._chatme;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Main_Controller implements Initializable {

    @FXML private TextArea chatArea;
    @FXML private TextField messageField;
    @FXML private Button sendButton;
    @FXML private Text portText;
    @FXML private ListView<String> clientList;

    public static TextArea staticChatArea;
    public static ListView<String> staticClientList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticChatArea = chatArea;
        staticClientList = clientList;
        portText.setText("Port Number : " + Login_Controller.IDPT + " | ID : " + Login_Controller.ID);
        messageField.setOnAction(e -> sendMessage());
    }

    @FXML
    private void sendMessage()
    {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                String formatted = "[" + Login_Controller.ID + "]: " + message;
                Client_X.out.println(formatted);

                // Echo your own message locally (server only broadcasts to others)
                chatArea.appendText(formatted + "\n");

                messageField.clear();
            } catch (Exception e) {
                chatArea.appendText("Error sending message: " + e.getMessage() + "\n");
            }
        }
    }
}