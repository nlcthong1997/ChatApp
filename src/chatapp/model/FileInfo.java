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
 * @author Dell
 */
public class FileInfo  implements Serializable {
    public File file;
    public byte[] dataBytes;

    public FileInfo(File file, byte[] dataBytes) {
        this.file = file;
        this.dataBytes = dataBytes;
    }

    public FileInfo() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public void setDataBytes(byte[] dataBytes) {
        this.dataBytes = dataBytes;
    }
    
    
}
