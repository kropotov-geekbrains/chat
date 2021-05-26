package ru.gb.chat.client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//Продолжаем изучение...
public class ClientChat extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root, 400, 275));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            NetworkService.close();
        });
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
