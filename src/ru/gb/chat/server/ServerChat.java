package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerChat {

    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args){
        new ServerChat().start();
    }

    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(8189)){
            System.out.println("Сервер запущен. Ожидается подключение...");

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключился");
                clients.add(new ClientHandler(socket, this));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Сервер закончил работу");
    }

    public void broadcastMsg(String msg){
        for (ClientHandler client : clients){
            client.sendMessage(msg);
        }
    }

    public void disconnectClient(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }
}
