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
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            users = (List<User>) ois.readObject();
            return users;
        } catch (IOException | ClassNotFoundException e) {
            return users;
        }
    }
}
