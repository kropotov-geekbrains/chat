package ru.gb.chat.server;

import ru.gb.chat.client.ClientChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem Kropotov on 17.05.2021
 */
public class ClientHandler {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private ServerChat serverChat;
    private ListAuthService authService = ListAuthService.getInstance();
    private User user;

    public ClientHandler(Socket socket, ServerChat serverChat) {
        try {
            this.socket = socket;
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            this.serverChat = serverChat;
            new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/auth ")) {
                            String[] token = msg.split("\\s");
                            User user = authService.findByLoginAndPassword(token[1], token[2]);
                            if (user != null && !serverChat.isNickBusy(user.getNickname())) {
                                sendMessage("/authok " + user.getNickname());
                                this.user = user;
                                serverChat.subscribe(this);
                                break;
                            }
                            if(user == null)
                                sendMessage("/authfail ");
                        }
                        if (msg.startsWith("/reg ")){
                            String[] token = msg.split("\\s");
                            ArrayList<User> users = (ArrayList<User>) authService.findAll((u)->{
                                if(u.getLogin().equals(token[1]))
                                    return true;
                                return false;
                            });
                            if(users != null){
                                sendMessage("r_fail ");
                            }
                            else{
                                User user = authService.save(new User(token[1],token[2],""));
                                sendMessage("r_ok ");
                            }
                        }
                    }
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/")) {
                            if (msg.equals("/end")) {
                                sendMessage("/end");
                                break;
                            }
                            if (msg.startsWith("/w")) {
                                String[] token = msg.split("\\s", 3);
                                serverChat.privateMsg(this, token[1], token[2]);

                            }
                            if(msg.equals("/del")){
                                sendMessage("/end");
                                authService.remove(user);
                                break;
                            }
                        } else {
                            serverChat.broadcastMsg(user.getNickname() + ": " + msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Клиент отключился");
                    disconnect();
                }
            }).start();
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        serverChat.unsubscribe(this);
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

    public User getUser() {
        return user;
    }
}