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
import static java.util.stream.Collectors.toList;



/**
 *
 * @author hd161
 */
public class RegisterController {
    public User register(String username, String password) throws IOException, FileNotFoundException, ClassNotFoundException {
        //get list user from file
        List<User> users = new ArrayList();
        users = FileCommon.readUsersFile("data.txt");
        
        List<User> result = new ArrayList();
        result = users.stream()
            .filter(user -> (user.username.equals(username)))
            .collect(toList());
        
        if (result.size() == 1) {
            return null;
        } else {
            User newUser = new User(username, password);
            users.add(newUser);
            FileCommon.writeUsersFile("data.txt", users);
            List<User> newUsers = new ArrayList();
            newUsers = FileCommon.readUsersFile("data.txt");
            List<User> rs = new ArrayList();
            rs = newUsers.stream()
            .filter(user -> (user.username.equals(username) && user.password.equals(password)))
            .collect(toList());
            if (rs.size() == 1) {
                return rs.get(0);
            }
        }
        return null;
    }
}
