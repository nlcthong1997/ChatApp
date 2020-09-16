/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.model;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author hd161
 */
public class Content implements Serializable {
    public String username;
    public String message;
    public int fromPort;
    public int toPort;
    public String action;
    public FileInfo fileInfo;

    public Content(String username, String message, int fromPort, int toPort, String action, FileInfo file) {
        super();
        this.username = username;
        this.message = message;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.action = action;
        this.fileInfo = file;
    }

    public FileInfo getFile() {
        return fileInfo;
    }

    public void setFile(FileInfo file) {
        this.fileInfo = file;
    }
    
    public Content() {
        super();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFromPort() {
        return fromPort;
    }

    public void setFromPort(int fromPort) {
        this.fromPort = fromPort;
    }

    public int getToPort() {
        return toPort;
    }

    public void setToPort(int toPort) {
        this.toPort = toPort;
    }
    
    
}
