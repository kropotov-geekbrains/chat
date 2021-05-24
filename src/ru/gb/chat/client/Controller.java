package ru.gb.chat.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final Map<String, Integer> uniqCheckMap = new HashMap<>();

    @FXML
    TextArea textArea;
    @FXML
    TextField textField;

    private final NetworkService networkService = new NetworkService();

    public void sendMsg(){
        String warning = validate();
        if (warning == null) {
            networkService.sendMessage(textField.getText());
            textField.clear();
        } else {
            new Alert(Alert.AlertType.WARNING, warning, ButtonType.OK).showAndWait();
        }

        textField.requestFocus();
    }



    private String validate() {
        String textFromField = textField.getText();
        String warning = null;
        if (textFromField.isEmpty()) {
            warning = "Нельзя отправлять пустое сообщение";
        } else {
            Integer count = uniqCheckMap.getOrDefault(textFromField, 0);
            if (count.equals(0)) {
                uniqCheckMap.clear();
            }
            uniqCheckMap.put(textFromField, ++count);
            if (count > 3) {
                warning = "Нельзя отправлять больше 3 одинаковых сообщений подряд. № вашей попытки: " + count;
            }
        }
        return warning;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Thread thread = new Thread(() -> {
            while (true) {
                textArea.appendText(networkService.getMessage() + "\n");
        }
        });
        thread.setDaemon(true);
        thread.start();



    }
}
