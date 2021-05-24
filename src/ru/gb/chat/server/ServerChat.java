package ru.gb.chat.server;

import com.sun.javafx.geom.AreaOp;

import java.io.IOException;
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }



  public void clientOff(ClientHandler client) {
        System.out.println("Список клиентов " + clients + ". отключаемый клиент " + client);
            clients.remove(client);
      System.out.println("Список клиентов " + clients);



  }


    public void broadcastMsg(String msg) {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }
}
