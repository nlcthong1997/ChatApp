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
            //đợi kết nối từ client
            Socket socket = server.accept();
            
            int clientPortId = socket.getPort();
            
            //server đọc data từ client và xử lý sau đó gửi đi
            ClientHandle handle = new ClientHandle(socket, clientPortId);
            
            //thêm vào danh sách show active
            listPortId += String.valueOf(clientPortId) + "#";
            listSocket.add(socket);
            
            //start thread
            handle.start();
        }
    }
    
    //main
    public static void main(String[] agrs) throws IOException {
        Server.listClient = new ArrayList();
        Server.listSocket = new ArrayList();
        Server.listPortId = "list@Active#";
        Server server = new Server(12345);
        server.start();
    }
}

class ClientHandle extends Thread {
    public Socket server;
    public int clientPortId;
    
    public ClientHandle(Socket server, int clientPortId) {
        this.server = server;
        this.clientPortId = clientPortId;
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
                
                if (mess.equals("load@Active")) {
                    SendClients send = new SendClients();
                    send.send();
                } else {
                    userName = arrStrMessage.nextToken();
                    portId = Integer.parseInt(arrStrMessage.nextToken());
                    for (Socket client: Server.listSocket) {
                        int portReciver = client.getPort();
                        System.out.println("Server read > client: " + portId + "> to: " + portReciver);
                        System.out.println("Server read1 > client: " + clientPortId + "> to: " + portReciver);
                        if (portReciver == portId || portReciver == clientPortId) {
                            System.out.println("Server send > " + userName + ": " + mess);
                            dos = new DataOutputStream(client.getOutputStream());
                            dos.writeUTF(portReciver + "#" + clientPortId + "#" + userName + ": " + mess);
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            //
        }
    }
}

class SendClients {
    public void send() {
        try {
            for (Socket client: Server.listSocket) {
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                System.out.println("Server gui list id");
                dos.writeUTF(Server.listPortId);
            }
        } catch (IOException e) {
            //
        }
    }
}