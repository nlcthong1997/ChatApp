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
            int clientPortId = socket.getPort();
            ClientHandle handle = new ClientHandle(socket, clientPortId);
            
            //thêm vào danh sách show active
            listPortId += String.valueOf(clientPortId) + "#";
            listClient.add(handle);
            listSocket.add(socket);
            
            //start thread
            handle.start();
        }
    }
    
    //main
    public static void main(String[] agrs) throws IOException {
        Server.listClient = new ArrayList();
        Server.listSocket = new ArrayList();
        Server.listPortId = ">>";
        Server server = new Server(12345);
        server.start();
    }
}

class ClientHandle extends Thread {
    private Socket server;
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
            dos = new DataOutputStream(server.getOutputStream());
            String received, mess, userName;
            int portId;
            while(true) {
                //đọc tin nhắn từ client
                received  = dis.readUTF();
                StringTokenizer arrStrMessage = new StringTokenizer(received , "#");
                mess = arrStrMessage.nextToken(); 
                userName = arrStrMessage.nextToken();
                portId = Integer.parseInt(arrStrMessage.nextToken());
                
//                System.out.println("Message: " + mess);
//                System.out.println("PortId: " + portId);
//                System.out.println("Username: " + userName);
                System.out.println(userName + ": " + mess);
                dos.writeUTF(userName + ": " + mess);
                for (ClientHandle client: Server.listClient) {
                    
//                    break;
                    
//                    
//                    System.out.println("Message from client: " + message);
                }
            }
            
        } catch (IOException e) {
            //
        }
        
        try {
            dis.close();
            dos.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
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