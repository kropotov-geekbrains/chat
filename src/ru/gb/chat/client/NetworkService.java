package ru.gb.chat.client;

import javax.naming.ldap.Control;
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


    private static Callback callOnException;
    private static Callback callOnMsgReceived;
    private static Callback callOnAuthenticated;
    private static Callback callOnDisconnect;

    static {
        Callback callback = (args) -> {};
        callOnException = callback;
        callOnMsgReceived = callback;
        callOnAuthenticated = callback;
        callOnDisconnect = callback;
    }

    public static void setCallOnException(Callback callOnException) {
        NetworkService.callOnException = callOnException;
    }

    public static void setCallOnMsgReceived(Callback callOnMsgReceived) {
        NetworkService.callOnMsgReceived = callOnMsgReceived;
    }

    public static void setCallOnAuthenticated(Callback callOnAuthenticated) {
        NetworkService.callOnAuthenticated = callOnAuthenticated;
    }

    public static void setCallOnDisconnect(Callback callOnDisconnect) {
        NetworkService.callOnDisconnect = callOnDisconnect;
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

    public static void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMessage() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            Thread clientListener = new Thread(() -> {
                try {
                    // цикл успешной аутентификации
                    String msg;
                    while (true) {
                        msg = in.readUTF();
                        if (msg.startsWith("/authok ")) {
                            callOnAuthenticated.callback(msg.split("\\s")[1]);
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
}
