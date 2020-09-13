package chatapp.common;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import chatapp.enumApp.ActionEnum;
import chatapp.model.ChatContent;
import chatapp.model.User;

class ReadServer extends Thread {
    private User server;
    private Socket socketS;
    
    public ReadServer(User uSer,Socket socket) {
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
                	Socket userReceiver = Server.mapSocket.get(chatContent.getReceiver());
                	 
                	System.out.println("User: "+Server.mapSocket.size());
                	
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