/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.common;

import chatapp.actionEnum.ActionEnum;
import chatapp.model.Content;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author Dell
 */
public class Server {
    final int port;
    public static ArrayList<Socket> listSocket;
    public static HashMap<Integer, String> listActive;
    
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
            System.out.println("clientPortId: " + clientPortId);
            //server đọc data từ client và xử lý sau đó gửi đi
            ClientHandle handle = new ClientHandle(socket, clientPortId);
            listSocket.add(socket);
            
            //start thread
            handle.start();
        }
    }
    
    //main
    public static void main(String[] agrs) throws IOException {
        Server.listSocket = new ArrayList();
        Server.listActive = new <Integer, String>HashMap();
        Server server = new Server(12345);
        server.start();
    }
}

class ClientHandle extends Thread {
//    public Socket server;
    public Socket socketReceiver;
    public int clientPortId;
    
//    public ClientHandle(Socket server, int clientPortId) {
//        this.server = server;
//        this.clientPortId = clientPortId;
//    
    public ClientHandle(Socket socketReceiver, int clientPortId) {
        this.socketReceiver = socketReceiver;
        this.clientPortId = clientPortId;
    }
    
    @Override
    public void run() {
        try {
            SendClients send = new SendClients();
            while (true) {
                ObjectInputStream objInputStream = new ObjectInputStream(socketReceiver.getInputStream());
                Object objReceiver = objInputStream.readObject();
                
                if (objReceiver == null) {
                    continue;
                }
                
                String action = String.valueOf(objReceiver);
                if (action.equals(ActionEnum.FIRSTCALL.getAction())) {
                    System.out.println("First call");
                    Content content = (Content) objInputStream.readObject();
                    //add list active
                    if (!Server.listActive.containsKey(content.fromPort)) {
                        Server.listActive.put(content.fromPort, content.username);
                    }
                    send.run();
                    continue;
                }
                
//                Content content = (Content) objReceiver;
                // client exit
//                if (content.action.equals(ActionEnum.EXITCHAT.getAction())) {
//                    //remove in list active
//                    if (Server.listActive.containsKey(content.fromPort)) {
//                        Server.listActive.remove(content.fromPort);
//                    }
//                    //remove in list socket client
//                    for (Socket client: Server.listSocket) {
//                        if (content.fromPort == client.getPort()) {
//                            Server.listSocket.remove(client);
//                        }
//                    }
//                    
//                    // send list
//                    send.run();
//                    break;   
//                }
//                System.out.println("Server> " + content.username + ": " + content.message);
                
                
                
                if (action.equals(ActionEnum.CLIENTSENDMESSAGE.getAction())) {
                    Content content = (Content) objInputStream.readObject();
                    //add list active
                    if (!Server.listActive.containsKey(content.fromPort)) {
                        Server.listActive.put(content.fromPort, content.username);
                    }
                    // send list
                    send.run();
                    
                    System.out.println("Server> " + content.username + ": " + content.message);
                    
                    // send for client
                    for (Socket client: Server.listSocket) {
                        System.out.println("Server> clientPort:" + client.getPort() + " >fromPort: " + content.fromPort + " >toPort: " + content.toPort);
                        if (content.toPort == client.getPort() || content.fromPort == client.getPort()) {
                            System.out.println("Server> send");
                            ObjectOutputStream objOutputStream = new ObjectOutputStream(client.getOutputStream());
                            objOutputStream.writeObject(ActionEnum.SENDMESSAGE.getAction());
                            objOutputStream.writeObject(content);
                            objOutputStream.flush();
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            //
        }
        
    }
}

class SendClients {
    public void run() {
        try {
            for (Socket client: Server.listSocket) {
                ObjectOutputStream objOutputStream = new ObjectOutputStream(client.getOutputStream());
                objOutputStream.writeObject(ActionEnum.UPDATEACTIVES.getAction());
                objOutputStream.writeObject(Server.listActive);
                objOutputStream.flush();
            }
        } catch (IOException e) {
            //
        }
    }
}