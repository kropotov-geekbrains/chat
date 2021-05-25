package client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.io.EOFException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private int msgCount = 0;
    private String newMsg;

    private boolean isConnected = true;

    @FXML
    TextArea textArea;

    @FXML
    TextField textField;


    private NetworkService networkService = new NetworkService();

    public void sendMsg(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UTILITY);

        String msg = newMsg;
        newMsg = textField.getText();
        if (newMsg.equals(msg)){
            msgCount++;
        } else msgCount = 0;

        if(textField.getText().equals("")){
            alert.setContentText("Пустое сообщение");
            alert.showAndWait();
        } else if(msgCount > 2){
            alert.setContentText("Сообщение отправлена более 3-х раз подряд");
            alert.showAndWait();
        } else networkService.sendMessage(textField.getText());

        textField.clear();
        textField.requestFocus();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            while (networkService.isConnected){
                textArea.appendText(networkService.getMassage() + "\n");
            }
            textArea.appendText("Клиент отключен");
            //textField.setVisible(false);
        }).start();
    }
}
