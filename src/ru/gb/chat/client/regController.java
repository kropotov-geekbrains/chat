package ru.gb.chat.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class regController {

    @FXML
    TextField regLogin;

    @FXML
    TextField regPassword;

    @FXML
    TextField regNickname;
    public void registration(ActionEvent actionEvent) {
        NetworkService.sendReg(regLogin.getText(), regPassword.getText(), regNickname.getText());
    }
}
