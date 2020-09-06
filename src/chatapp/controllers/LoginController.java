/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.controllers;
import chatapp.common.FileCommon;
import chatapp.common.Server;
import chatapp.model.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
/**
 *
 * @author Dell
 */
public class LoginController {
    public User login(String username, String password) throws IOException, FileNotFoundException, ClassNotFoundException {
        //get list user from file
        List<User> users = new ArrayList();
        users = FileCommon.readUsersFile("data.txt");
        //check user
        List<User> result = new ArrayList();
        result = users.stream()
            .filter(user -> (user.username.equals(username) && user.password.equals(password)))
            .collect(toList());
        if (result.size() == 1) {
            //start server
            System.out.println(Server.running);
            Ser
            if (!Server.running) {
                Server server = new Server(12345);
                server.start();
            }
            return result.get(0);
        }
        return null;
    }
}
