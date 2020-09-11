/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class Server {
    private int port;
    public static ArrayList<ClientHandle> listClient;
    public static ArrayList<Socket> listSocket;
    public static String listPortId;
    
    public Server(int port) {
        this.port = port;
    }
    
    public void start() throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is running on port: " + port);
        
        while (true) {
            //send all client
            SendClients send = new SendClients();
            send.start();
            
            //đợi kết nối từ client
            Socket socket = server.accept();
            
            //server đọc data từ client và xử lý sau đó gửi đi
            ClientHandle handle = new ClientHandle(socket);
            
            //thêm vào danh sách show active
            int clientPortId = socket.getPort();
            listPortId += String.valueOf(clientPortId) + "#";
//            listClient.add(handle);
            listSocket.add(socket);
            
            //start thread
            handle.start();
        }
    }
    
    //main
    public static void main(String[] agrs) throws IOException {
        Server.listClient = new ArrayList();
        Server.listSocket = new ArrayList();
        Server.listPortId = "listActive#";
        Server server = new Server(12345);
        server.start();
    }
}

class ClientHandle extends Thread {
    public Socket server;
    
    public ClientHandle(Socket server) {
        this.server = server;
    }
    
    @Override
    public void run() {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        try {
            dis = new DataInputStream(server.getInputStream());
            String received, mess, userName;
            int portId;
            while(true) {
                //đọc tin nhắn từ client
                received  = dis.readUTF();
                StringTokenizer arrStrMessage = new StringTokenizer(received , "#");
                mess = arrStrMessage.nextToken(); 
                userName = arrStrMessage.nextToken();
                portId = Integer.parseInt(arrStrMessage.nextToken());
                
                for (Socket client: Server.listSocket) {
                    int portReciver = client.getPort();
                    if (portReciver == portId) {
                        dos = new DataOutputStream(server.getOutputStream());
                        dos.writeUTF(server.getPort() +">>>"+ userName + ": " + mess + ">>>" + client.getPort());
                        break;
                    }
                }
            }
            
        } catch (IOException e) {
            //
        }
    }
}

class SendClients extends Thread {
    @Override
    public void run() {
        try {
            for (Socket client: Server.listSocket) {
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF(Server.listPortId);
            }
        } catch (IOException e) {
            //
        }
    }
}