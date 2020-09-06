/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.controllers;
import chatapp.common.FileCommon;
import chatapp.model.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Dell
 */
public class LoginController {
    public static void login() throws IOException, FileNotFoundException, ClassNotFoundException {
        List<User> users = new ArrayList();
        for (int i = 0; i <= 5; i++) {
            User user = new User("chithong" + String.valueOf(i), "123456");
            users.add(user);
        }
        FileCommon.writeFile("data.txt", users);
        
        List<User> users1 = new ArrayList();
        users1 = FileCommon.readFile("data.txt");
        for (User user : users1) {
            System.out.println(user.getUsername());
            System.out.println(user.getPassword());
        }
    }
}
