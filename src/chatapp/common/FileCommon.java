/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import chatapp.model.User;
import java.util.ArrayList;

/**
 *
 * @author Dell
 */
public class FileCommon {
    public static void writeUsersFile(String fileName, List<User> users) throws FileNotFoundException, IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(users);
        }
    }
    
    public static List<User> readUsersFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        List<User> users = new ArrayList();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        users = (List<User>) ois.readObject();
        return users;
    }
    
    public static void main(String[] args) {
    	List<User> lsUser= new ArrayList<User>();
		for(int i=0;i<10;i++) {
			String username="username"+String.valueOf(i);
			String password="password"+String.valueOf(i);
			User a= new User(username, password);
			lsUser.add(a);
		}
		try {
			FileCommon.writeUsersFile("data.txt",lsUser);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
