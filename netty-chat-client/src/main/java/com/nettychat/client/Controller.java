package com.nettychat.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Network network;

    @FXML
    TextField msg;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network((args -> {
            mainArea.appendText((String)args[0]);
        }));
    }

    public void sendMessage(ActionEvent actionEvent) {
        network.send(msg.getText());
        msg.clear();
        msg.requestFocus();
    }

    public void exit(ActionEvent actionEvent) {
        network.close();
        Platform.exit();
    }
}
