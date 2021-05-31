package ru.gb.chat.client;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    private final Map<String, Integer> uniqCheckMap = new HashMap<>();

    @FXML
    TextArea textArea;
    @FXML
    TextField textField;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passField;

    @FXML
    HBox authPanel, msgPanel;

    @FXML
    ListView<String> clientsList;

    private String nickname;
    private boolean authenticated;

    private IdleService idleService;

    public void sendMsg(){
        String warning = validate();
        if (warning == null) {
            String text = textField.getText();
            if (text.equals("/end")) {
                Platform.runLater(() -> {
                    idleService.cancel();
                });
            }
            else {
                Platform.runLater(() -> {
                    idleService.restart();
                });
            }
            NetworkService.sendMessage(text);
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
        idleService = new IdleService();
        idleService.setPeriod(Duration.seconds(20));
        idleService.setDelay(Duration.seconds(20));

        setAuthenticated(false);
        clientsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedNickname = clientsList.getSelectionModel().getSelectedItem();
                textField.setText("/w " + selectedNickname + " ");
                textField.requestFocus();
                textField.selectEnd();
            }
        });

        setCallbacks();

    }

    public void sendAuth() {
        NetworkService.sendAuth(loginField.getText(), passField.getText());
        loginField.clear();
        passField.clear();
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientsList.setVisible(authenticated);
        clientsList.setManaged(authenticated);
        if (!authenticated) {
            nickname = "";
            Platform.runLater(() -> {
                idleService.cancel();
            });

        }
        else {
            Platform.runLater(() -> {
                idleService.restart();
            });
        }

    }

    public void setCallbacks() {
        NetworkService.setCallOnException(args -> new Alert(Alert.AlertType.WARNING, String.valueOf(args[0]), ButtonType.OK).showAndWait());

        NetworkService.setCallOnWrongAuthenticated(args -> {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.WARNING, String.valueOf(args[0]), ButtonType.OK).showAndWait();
                });
        });

        NetworkService.setCallOnDoCreateAccount(args -> {
            Platform.runLater(() -> {

                String message = String.valueOf(args[0]);

                String [] tokens = message.split ("\"");

                Alert alert =  new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.CANCEL);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                Optional<ButtonType> result =alert.showAndWait();

                if (result.orElse(ButtonType.CANCEL) == ButtonType.YES) {
                    System.out.println("Создаем аккаунт");
                    createAccount(tokens[1],tokens[3], tokens[1]);
                }
            });
        });


        NetworkService.setCallOnAuthenticated(args -> {
            nickname = String.valueOf(args[0]);
            setAuthenticated(true);

        });

        NetworkService.setCallOnMsgReceived(args -> {
            String msg = String.valueOf(args[0]);
            if (msg.startsWith("/")) {
                if (msg.startsWith("/clients")) {
                    String[] nicknames = msg.split("\\s");
                    Platform.runLater(() -> {
                        clientsList.getItems().clear();
                        for (int i = 1; i < nicknames.length; i++) {
                            clientsList.getItems().add(nicknames[i]);
                        }
                    });
                }
            } else {
                textArea.appendText(msg + "\n");
            }
        });

        NetworkService.setCallOnDisconnect(args -> setAuthenticated(false));
    }
    public void deleteAccount(){
      textField.setText("/delacc");
      sendMsg();
    }

    public void createAccount(String login, String password, String nicname){
        textField.setText("/createacc "+login+ " "+password+ " "+nicname);
        sendMsg();
    }


    private class IdleService extends ScheduledService<Void> {

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {

                @Override
                protected Void call() throws Exception {
                    System.out.println("Idle");
                    textField.setText("/end");
                    sendMsg();
                    return null;
                }
            };
        }
    }
}
