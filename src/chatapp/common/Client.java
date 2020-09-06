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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class Client {
    private InetAddress host;
    private int port;
    
    public Client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void start() throws IOException {
        Socket client = new Socket(host, port);
        Read read = new Read(client);
        Write write = new Write(client);
        read.start();
        write.start();
    }
}

class Read extends Thread {
    private Socket client;
    
    public Read(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(client.getInputStream());
            while(true) {
                String message = dis.readUTF(); //doc tu server
                System.out.println(message);
            }
        } catch (Exception e) {
            try {
                dis.close();
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class Write extends Thread {
    private Socket client;
    
    public Write(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        DataOutputStream dos = null;
        Scanner sc = null;
        try {
            dos = new DataOutputStream(client.getOutputStream());
            sc = new Scanner(System.in);
            while(true) {
                String message = sc.nextLine();
                dos.writeUTF(message); //ghi len server
            }
        } catch (IOException ex) {
            Logger.getLogger(Write.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
