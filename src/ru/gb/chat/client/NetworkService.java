package client;

import server.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkService {
    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;

    private Socket socket;
    private DataInputStream in ;
    private DataOutputStream out;

    public boolean isConnected = true;


    public NetworkService(){
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
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
                System.out.println(1);
                isConnected = false;
            }
        return "";
    }


}
