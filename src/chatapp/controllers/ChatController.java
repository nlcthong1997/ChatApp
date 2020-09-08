/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.controllers;

import chatapp.common.Client;
import chatapp.model.User;
import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author Dell
 */
public class ChatController {
    public void startSocket(User user) throws IOException {
        Client client = new Client(InetAddress.getLocalHost(),12345, user);
        client.start();
    }
}
