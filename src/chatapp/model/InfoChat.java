/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.model;

/**
 *
 * @author Dell
 */
public class InfoChat {
    public int portSend;
    public int portReciver;
    public String username;

    public InfoChat(int portSend, int portReciver, String username) {
        this.portSend = portSend;
        this.portReciver = portReciver;
        this.username = username;
    }

    public int getPortSend() {
        return portSend;
    }

    public void setPortSend(int portSend) {
        this.portSend = portSend;
    }

    public int getPortReciver() {
        return portReciver;
    }

    public void setPortReciver(int portReciver) {
        this.portReciver = portReciver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
