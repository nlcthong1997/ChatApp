/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Dell
 */
public class Client {
    public Socket client;
    
    public Client(Socket client) {
        this.client = client;
    }
    
    public String read() throws IOException {
        ReadMessage read = new ReadMessage(client);
        read.start();
        return read.getMessage();
    }
    
    public void send(String message) throws IOException {
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        dos.writeUTF(message);
    }
}

class ReadMessage extends Thread {
    private Socket client;
    public static String message;
    
    public ReadMessage(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(client.getInputStream());
                String mess = dis.readUTF();
                System.out.println("client nhan: " + mess);
                this.message = mess;
        } catch (Exception e) {
            //
        }
    }
    
    public String getMessage() {
        return this.message;
    }
}

class SendMessage extends Thread {
    private Socket client;
    private String message;
    
    public SendMessage(Socket client, String message) {
        this.client = client;
        this.message = message;
    }
    
    @Override
    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            dos.writeUTF(message);
        } catch (IOException ex) {
            //
        }
    }
}
