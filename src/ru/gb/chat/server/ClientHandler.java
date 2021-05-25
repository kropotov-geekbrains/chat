package server;

import client.NetworkService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket, ServerChat serverChat){
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while(true){
                        String str = in.readUTF();
                        if(str.equals("/end")){
                            serverChat.disconnectClient(this);
                            break;
                        }
                        System.out.println("Client send: " + str);

                        serverChat.broadcastMsg(str);
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

    public void disconnect(){
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

    public void sendMessage(String message){
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMassage(){
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
