package ru.gb.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Artem Kropotov on 17.05.2021
 */
public class ServerChat {
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        new ServerChat().start();
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключился");
                clients.add(new ClientHandler(socket, this));

                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).getSocket().isClosed()) {
                        clients.remove(i);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }
}
