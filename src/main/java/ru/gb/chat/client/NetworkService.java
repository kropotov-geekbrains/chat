package ru.gb.chat.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Artem Kropotov on 17.05.2021
 */
public class NetworkService {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 8189;

    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;


    @Setter private static Callback callOnException;
    @Setter private static Callback callOnMsgReceived;
    @Setter private static Callback callOnAuthenticated;
    @Setter private static Callback callOnDisconnect;
    @Setter private static Callback callOnRegFailed;
    @Setter private static Callback callOnAuthFailed;

    private static Wait_Count waitTillActive;
    private static final int WAIT_TO_MS = 30000;
    private static final int WAIT_STEP_MS = 50;

    static {
        Callback callback = (args) -> {};
        callOnException = callback;
        callOnMsgReceived = callback;
        callOnAuthenticated = callback;
        callOnDisconnect = callback;
        callOnRegFailed = callback;
        callOnAuthFailed = callback;
        waitTillActive = new Wait_Count(WAIT_TO_MS, WAIT_STEP_MS,(arg)->{
            disconnect();
            waitTillActive.stopW();
        });
    }

    public static void sendAuth(String login, String password) {
        try {
            if (socket == null || socket.isClosed()) {
                connect();
            }
            out.writeUTF("/auth " + login + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendReg(String login, String password) {
        try {
            if (socket == null || socket.isClosed()) {
                connect();
            }
            out.writeUTF("/reg " + login + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message) {
        waitTillActive.update();
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            Thread clientListener = new Thread(() -> {
                try {
                    // цикл успешной аутентификации
                    waitTillActive.startW();
                    String msg;
                    while (true) {
                        msg = in.readUTF();
                        if (msg.startsWith("/authok ")) {
                            callOnAuthenticated.callback(msg.split("\\s")[1]);
                            break;
                        }
                        if (msg.startsWith("/authfail ")) {
                            callOnAuthFailed.callback();
                            break;
                        }
                        if (msg.startsWith("/r_fail ")) {
                            callOnRegFailed.callback();
                            break;
                        }
                    }
                    while (true) {
                        msg = in.readUTF();
                        if (msg.equals("/end")) {
                            break;
                        }
                        callOnMsgReceived.callback(msg);
                    }
                } catch(IOException e) {
                    callOnException.callback("Соединение с сервером разорвано");
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            });
            clientListener.setDaemon(true);
            clientListener.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void disconnect() {
        callOnDisconnect.callback();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        Platform.runLater( () -> {
            Platform.exit();
            sendMessage("/end");
        });
    }
}
