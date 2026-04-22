package me._chatme;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login_Controller extends Application implements Initializable
{
    @FXML
    TextField IDPTtx;
    @FXML
    TextField IDtx;
    @FXML
    Label IDv;
    @FXML
    Label Portv;


    static int IDPT;
    static int ID;

    @FXML
    void Save()
    {
        ID = Integer.parseInt(IDtx.getText());
        IDPT = Integer.parseInt(IDPTtx.getText());
        IDv.setText(String.valueOf(ID));
        Portv.setText(String.valueOf(IDPT));
        Alert_Con("Saved");
    }

    @FXML
    void Check_Validate()
    {
        IDPT = Integer.parseInt(IDPTtx.getText());

        /*making sure the user enter the last 5 digits of his ID.
         if not, we check if the port number is less than 1024 since its 4 digits*/
        if (String.valueOf(Math.abs(IDPT)).length() < 5)
        {
            if (IDPT < 1024) // 1 - 1023 ports
            {
                Alert_Con("uh oh, your ID is in the range of Standard Ports that are not for custom use, \n don't worry I got you,I'll just add 10,000 to your number");
                IDPT += 1000;
            } else//if it is not less than 1024 than it can't work since the port number is most likely from 1 to 1023 which cannot be used by the user
            {
                Alert_ERR("uh oh you enter more than 5 digits make sure you enter the right number before proceeding.");

            }
        }
        else if (IDPT > 65535) //port limit
            {
                Alert_ERR("uh oh your port is out of range, make sure you enter the last 4 digits of your ID instead.");
            }
        else
        {
            connect();
        }

    }

    private void Alert_Con(String s)
    {
        Alert Alt = new Alert(Alert.AlertType.CONFIRMATION,s);
        Alt.show();
    }

    private void Alert_ERR(String s)
    {
        Alert Alt = new Alert(Alert.AlertType.ERROR,s);
        Alt.show();
    }



    public void connect()
    {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main Page.fxml")));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("ChatME - Main Chat");
            stage.setScene(scene);
            stage.show();
            Client_X.startConnection();
        } catch (IOException e) {
            System.err.println("Error Loading Main Page FXML : " + e);
            e.printStackTrace();
        }
    }



    @Override
    public void start(Stage primaryStage) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IDtx.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ID = Integer.parseInt(newValue);
                IDv.setText(String.valueOf(ID));
                Platform.runLater(() -> {
                    if (IDv.getScene() != null) {
                        Stage stage = (Stage) IDv.getScene().getWindow();
                        stage.sizeToScene();
                    }
                });
            } catch (NumberFormatException e) {
                IDv.setText("#");
                Platform.runLater(() -> {
                    if (IDv.getScene() != null) {
                        Stage stage = (Stage) IDv.getScene().getWindow();
                        stage.sizeToScene();
                    }
                });
            }
        });

        IDPTtx.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                IDPT = Integer.parseInt(newValue);
                Portv.setText(String.valueOf(IDPT));
                Platform.runLater(() -> {
                    if (Portv.getScene() != null) {
                        Stage stage = (Stage) Portv.getScene().getWindow();
                        stage.sizeToScene();
                    }
                });
            } catch (NumberFormatException e) {
                Portv.setText("#");
                Platform.runLater(() -> {
                    if (Portv.getScene() != null) {
                        Stage stage = (Stage) Portv.getScene().getWindow();
                        stage.sizeToScene();
                    }
                });
            }
        });
    }
}
