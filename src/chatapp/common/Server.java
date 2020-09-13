/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chatapp.enumApp.ActionEnum;
import chatapp.model.ChatContent;
import chatapp.model.User;

/**
 *
 * @author Dell
 */
public class Server {
    private int port;
    public static volatile Map<User,Socket> mapSocket = new HashMap<User,Socket>();
    
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
            
            InputStream inputStream = socket.getInputStream();
	        // create a DataInputStream so we can read data from it.
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
          
           
			try {
				 User userConnectRecently = (User) objectInputStream.readObject();
				 mapSocket.put(userConnectRecently, socket);
				 
				 // send to all user that update list user
				 
				 for (Map.Entry<User, Socket> en :mapSocket.entrySet() ) {
					// get the output stream from the socket.
	                 OutputStream outputStream = en.getValue().getOutputStream();
	                 // create an object output stream from the output stream so we can send an object through it
	                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
	                 String updateListUser=new String(ActionEnum.UPDATELISTUSER.getAction());
	                 objectOutputStream.writeObject(updateListUser);
	                 objectOutputStream.flush();
	                 
	                 // send list user
	                 ArrayList<User> lsUser = new ArrayList<User>(mapSocket.keySet());
	                 objectOutputStream.writeObject(lsUser);
	                 objectOutputStream.flush();
				 }
				 ReadServer2 read = new ReadServer2(userConnectRecently,socket);
		         read.start();
			} catch (ClassNotFoundException e) {
				socket.close();
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				socket.close();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    //start server
    public static void main(String[] agrs) throws IOException {
         
        Server server = new Server(1234);
        server.start();
    }
    
    
    class ReadServer2 extends Thread {
        private User server;
        private Socket socketS;
        
        public ReadServer2(User uSer,Socket socket) {
            this.server = uSer;
            this.socketS= socket;
        }
        
        @Override
        public void run() {
        	
            try {
            	 InputStream inputStream = socketS.getInputStream();
                while(true) {
                	// create a DataInputStream so we can read data from it.
               	 	ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                	ChatContent chatContent= (ChatContent) objectInputStream.readObject();
                	System.out.println("User: "+chatContent.getSender().username +" is out chat");
                	if(chatContent != null) {
                		if(ActionEnum.EXITCHAT.equals(chatContent.getContentChat())){
                    		System.out.println("User: "+chatContent.getSender().username +" is out chat");
                    		// update list
                    		break;
                    	}
                    	
                        // send chat to user has sent
                    	Socket userReceiver = getMap(chatContent.getReceiver());
                    	 
                    	System.out.println("chatContent.getReceiver(): "+chatContent.getReceiver().getUsername());
                    	
                    	System.out.println("User: "+Server.mapSocket.size());
                    	
                    	System.out.println("User: "+userReceiver);
                    	
                    	// get the output stream from the socket.
                        OutputStream outputStream = userReceiver.getOutputStream();
                        // create an object output stream from the output stream so we can send an object through it
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                        
                        String updateChat= ActionEnum.UPDATECHAT.getAction();
                        objectOutputStream.writeObject(updateChat);
                        objectOutputStream.flush();
                        
                        objectOutputStream.writeObject(chatContent);
                        objectOutputStream.flush();
                	}
                }
            } catch (Exception e) {
                
            }
        }
    }
    
   static Socket getMap(User user) {
	   for (Map.Entry<User, Socket> en :mapSocket.entrySet() ) {
		   
		   if(en.getKey().getUsername().equals(user.getUsername())) {
			   return en.getValue();
		   }
	   }
	   return null;
   }
}
 