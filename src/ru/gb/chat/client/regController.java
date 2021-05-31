package ru.gb.chat.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class regController {

    @FXML
    TextField regLogin;

    @FXML
    TextField regPassword;

    @FXML
    TextField regNickname;
    public void registration(ActionEvent actionEvent) {
        NetworkService.sendReg(regLogin.getText(), regPassword.getText(), regNickname.getText());
            regLogin.clear();
            regNickname.clear();
            regPassword.clear();
    }
}

