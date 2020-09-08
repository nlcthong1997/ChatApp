/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.common;

import chatapp.model.InfoChat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class Server {
    private int port;
    public static ArrayList<Socket> listSocket;
    public static ArrayList<InfoChat> listInfoChat;
    
    public Server(int port) {
        this.port = port;
    }
    
    public void start() throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is running on port: " + port);
        //WriteServer write = new WriteServer();
        //write.start();
        while (true) {
            Socket socket = server.accept(); //đợi kết nối từ client
            listSocket.add(socket); //thêm vào danh sách các kết nối
            ReadServer read = new ReadServer(socket);
            read.start();
        }
    }
    
    //start server
    public static void main(String[] agrs) throws IOException {
        Server.listSocket = new ArrayList();
        Server.listInfoChat = new ArrayList();
        Server server = new Server(12345);
        server.start();
    }
}

class ReadServer extends Thread {
    private Socket server;
    
    public ReadServer(Socket server) {
        this.server = server;
    }
    
    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(server.getInputStream());
            while(true) {
                String message = dis.readUTF(); //đọc tin nhắn từ client
                for (Socket item: Server.listSocket) {
                    DataOutputStream dos = new DataOutputStream(server.getOutputStream());
                    dos.writeUTF(message);//ghi các tin nhắn lên các client trong danh sách.
                }
            }
        } catch (Exception e) {
            try {
                dis.close();
                server.close();
            } catch (IOException ex) {
                Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class WriteServer extends Thread {
    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            String message = sc.nextLine();
            try {
                for (Socket item: Server.listSocket) {
                    DataOutputStream dos = new DataOutputStream(item.getOutputStream());
                    dos.writeUTF(message);
                }
            } catch (IOException ex) {
                Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}