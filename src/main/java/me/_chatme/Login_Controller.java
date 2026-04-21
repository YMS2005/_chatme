package me._chatme;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class Login_Controller {
    @FXML
    TextField IDPTtx;
    @FXML
    TextField IDtx;


    @FXML
    void Check_Port()
    {
        int IDPT = Integer.parseInt(IDPTtx.getText());

        /*making sure the user enter the last 5 digits of his ID.
         if not, we check if the port number is less than 1024 since its 4 digits*/
        if (String.valueOf(Math.abs(IDPT)).length() < 5) {
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
}



