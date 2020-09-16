/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.view;

import chatapp.actionEnum.ActionEnum;
import chatapp.model.User;
import chatapp.model.Content;
import chatapp.model.FileInfo;
import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Dell
 */
public class ChatView extends javax.swing.JFrame implements Runnable{

    /**
     * Creates new form ChatView
     */
    public static User user;
    public int serverPort = 12345;
    public static Socket socket;
    private static HashMap<Integer, String> listActive;
    private static HashMap<String, String> listChat;
    private static HashMap<String, FileInfo> files;
    
    public static int selectedUser = 0;
    public ObjectInputStream objInputStream;
    public ObjectOutputStream objOutputStream;
    
    public ChatView(User _user) throws IOException {
        initComponents();
        ChatView.user = _user;
        username.setText(_user.username);
        view_chat.setEditable(false);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            Socket _socket = new Socket(InetAddress.getLocalHost(), serverPort);
            ChatView.socket = _socket;
            System.out.println("InetAddress.getLocalHost(): " + InetAddress.getLocalHost());
            objOutputStream = new ObjectOutputStream(ChatView.socket.getOutputStream());
            Content first = new Content();
            first.action = "first-call";
            first.fromPort = ChatView.socket.getLocalPort();
            first.toPort = 0;
            first.message = "";
            first.username = user.username;
            first.fileInfo = null;
            
            objOutputStream.writeObject(ActionEnum.FIRSTCALL.getAction());
            objOutputStream.writeObject(first);
            objOutputStream.flush();
            
            DefaultListModel defaultListModel = new DefaultListModel();
            DefaultListModel listFileModel = new DefaultListModel();
            ChatView.listChat = new HashMap<String, String>();
            ChatView.files = new HashMap<String, FileInfo>();
            
            while (true) {
                //server send block 1
                String action = ChatView.loadActive(defaultListModel);
                
                if (ActionEnum.FIRSTCALL.getAction().equals(action)) {
                    continue;
                }
                
                if (ActionEnum.EXITCHAT.getAction().equals(action)) {
                    ObjectInputStream objExit = new ObjectInputStream(ChatView.socket.getInputStream());
                    Object receiverExit = objExit.readObject();
                    
                    int portExit = Integer.parseInt((String) receiverExit);
                    String keyExit = String.valueOf(portExit) + "#" + String.valueOf(ChatView.socket.getLocalPort()) + "#";
                    String reverseKeyExit = String.valueOf(ChatView.socket.getLocalPort()) + "#" + String.valueOf(portExit) + "#";
                    
                    //remove list chat
                    for (Map.Entry chat: listChat.entrySet()) {
                        if (chat.getKey().equals(keyExit)) {
                            ChatView.listChat.remove(keyExit);
                        }
                        if (chat.getKey().equals(reverseKeyExit)) {
                            ChatView.listChat.remove(reverseKeyExit);
                        }
                    }
                    continue;
                }
                
                //server send block 2
                ObjectInputStream objInputContent = new ObjectInputStream(ChatView.socket.getInputStream());
                Object receiverContent = objInputContent.readObject();
                
                String actionContent = String.valueOf(receiverContent);
                
                if (ActionEnum.CONTINUE.getAction().equals(actionContent)) {
                    continue;
                }
                
                if (ActionEnum.SERVERSENDMESSAGE.getAction().equals(actionContent)) {
                    Content content = (Content) objInputContent.readObject();
                    
                    String key = String.valueOf(content.fromPort) + "#" + String.valueOf(content.toPort) + "#";
                    String reverseKey = String.valueOf(content.toPort) + "#" + String.valueOf(content.fromPort) + "#";
                    String oldChat, newChat;
                    if (!ChatView.listChat.containsKey(key) && !ChatView.listChat.containsKey(reverseKey)) {
                        ChatView.listChat.put(key, content.username + ": " + content.message + "\n");
                    } else if (ChatView.listChat.containsKey(key)){
                        oldChat = ChatView.listChat.get(key);
                        newChat = oldChat + content.username + ": " + content.message + "\n";
                        ChatView.listChat.replace(key, oldChat, newChat);
                    } else {
                        oldChat = ChatView.listChat.get(reverseKey);
                        newChat = oldChat + content.username + ": " + content.message + "\n";
                        ChatView.listChat.replace(reverseKey, oldChat, newChat);
                    }
                    
                    // concept: save only file do not exists in session
                    if (content.fileInfo != null) {
                        String fileName = content.fileInfo.file.getName() + "#";
                        if (!ChatView.files.containsKey(key + fileName) && !ChatView.files.containsKey(reverseKey + fileName)) {
                            ChatView.files.put(key + fileName, content.fileInfo);
                        }
                    }
                    
                    // server send it to yourself
                    if (content.fromPort == ChatView.socket.getLocalPort()) {
                        //handle users active
                        int index = 0;
                        for (Map.Entry userActive: listActive.entrySet()) {
                            if (userActive.getKey().equals(content.toPort)) {
                                ChatView.list_user.setSelectedIndex(index);
                                ChatView.selectedUser = (int) userActive.getKey();
                            }
                            if (!userActive.getKey().equals(ChatView.socket.getLocalPort())) {
                                index++;
                            }
                        }
                        // handle message
                        String contentChat;
                        if (ChatView.listChat.containsKey(key)) {
                            contentChat = listChat.get(key);
                        } else {
                            contentChat = listChat.get(reverseKey);
                        }
                        view_chat.setText(contentChat);
                        
                    } else { // server send for user receiver
                        int index = 0;
                        int selected = 0;
                        // user focus
                        for (Map.Entry userActive: listActive.entrySet()) {
                            if (userActive.getKey().equals(ChatView.selectedUser)) {
                                ChatView.list_user.setSelectedIndex(index);
                                selected = (int) userActive.getKey();
                                ChatView.selectedUser = (int) userActive.getKey();
                            }
                            // expect current user
                            if (!userActive.getKey().equals(ChatView.socket.getLocalPort())) {
                                index++;
                            }
                        }
                        if (selected != 0) {
                            String keyLoad = selected + "#" + String.valueOf(ChatView.socket.getLocalPort()) + "#";
                            String reverseKeyLoad = String.valueOf(ChatView.socket.getLocalPort()) + "#" + selected + "#";
                            String contentChat;
                            if (ChatView.listChat.containsKey(keyLoad)) {
                                contentChat = listChat.get(key);
                            } else {
                                contentChat = listChat.get(reverseKeyLoad);
                            }
                            view_chat.setText(contentChat);
                        }
                        
                    }
                    
                    //handle file
                    String keyFile = ChatView.socket.getLocalPort() + "#" + ChatView.selectedUser + "#";
                    String reverseKeyFile = ChatView.selectedUser + "#" + ChatView.socket.getLocalPort() + "#";
                    
                    listFileModel.removeAllElements();
                    for (Map.Entry file: files.entrySet()) {
                        FileInfo fileInfo = (FileInfo) file.getValue();
                        String name = fileInfo.file.getName();
                        String keyFileTemp = keyFile + name + "#";
                        String reverseKeyFileTemp = reverseKeyFile + name + "#";
                        if (keyFileTemp.equals(file.getKey()) || reverseKeyFileTemp.equals(file.getKey())) {
                            listFileModel.addElement(name);
                        }
                    }
                    list_file.setModel(listFileModel);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            //
        }
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openFileChooser = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_user = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        view_chat = new javax.swing.JTextArea();
        txt_chat = new javax.swing.JTextField();
        btn_send = new javax.swing.JButton();
        username = new javax.swing.JLabel();
        btn_send_file = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_file = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        btn_save = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(609, 404));
        setMinimumSize(new java.awt.Dimension(609, 404));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        list_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_userMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(list_user);

        view_chat.setColumns(20);
        view_chat.setRows(5);
        jScrollPane2.setViewportView(view_chat);

        btn_send.setBackground(new java.awt.Color(0, 102, 204));
        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        username.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        username.setForeground(new java.awt.Color(0, 0, 204));
        username.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        btn_send_file.setText("...");
        btn_send_file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_send_fileActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Users live");

        jScrollPane3.setViewportView(list_file);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Files receiver");

        btn_save.setText("Save file");
        btn_save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_saveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(0, 27, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_send_file, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btn_save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_send_file, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1))
                .addGap(20, 20, 20))
        );

        btn_send_file.getAccessibleContext().setAccessibleName("File");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void list_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_userMouseClicked
        String selected = list_user.getSelectedValue();
        if (selected != null) {
            StringTokenizer strToken = new StringTokenizer(selected , "#");
            int portUser = Integer.parseInt(strToken.nextToken());
            ChatView.selectedUser = portUser;
            
            String key = String.valueOf(portUser) + "#" + String.valueOf(ChatView.socket.getLocalPort()) + "#";
            String reverseKey = String.valueOf(ChatView.socket.getLocalPort()) + "#" + String.valueOf(portUser) + "#";
            String keyTemp = "";
            
            //list chat
            if (ChatView.listChat.containsKey(key)) {
                keyTemp = key;
            }
            if (ChatView.listChat.containsKey(reverseKey)) {
                keyTemp = reverseKey;
            }
            
            if (listChat.containsKey(keyTemp)) {
                String content = listChat.get(keyTemp);
                view_chat.setText(content);
            } else {
                view_chat.setText("");
            }
            
            //list file
            DefaultListModel listFileModel = new DefaultListModel();
            listFileModel.removeAllElements();
            for (Map.Entry file: files.entrySet()) {
                FileInfo fileInfo = (FileInfo) file.getValue();
                String name = fileInfo.file.getName();
                String keyFileTemp = key + name + "#";
                String reverseKeyFileTemp = reverseKey + name + "#";
                if (keyFileTemp.equals(file.getKey()) || reverseKeyFileTemp.equals(file.getKey())) {
                    listFileModel.addElement(name);
                }
            }
            list_file.setModel(listFileModel);
            
        }
    }//GEN-LAST:event_list_userMouseClicked

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        try {
            String message = txt_chat.getText();
            
            if (!message.equals("") && ChatView.selectedUser != 0) {
                Content data = new Content();
                data.action = "send-message";
                data.fromPort = ChatView.socket.getLocalPort();
                data.toPort = ChatView.selectedUser;
                data.message = message;
                data.username = user.username;
                data.fileInfo = null;
                
                objOutputStream = new ObjectOutputStream(ChatView.socket.getOutputStream());
                objOutputStream.writeObject(ActionEnum.CLIENTSENDMESSAGE.getAction());
                objOutputStream.writeObject(data);
                objOutputStream.flush();
                
                txt_chat.setText("");
                System.out.println("send");
            }
        } catch (IOException | NumberFormatException e) {
        }
        
        
    }//GEN-LAST:event_btn_sendActionPerformed

    private void btn_send_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_send_fileActionPerformed
        try {
            if (ChatView.selectedUser != 0) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(openFileChooser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    //create file
                    File file = new File(fc.getSelectedFile().getPath());
                    //create array byte
                    byte [] dataBytes  = new byte [(int)file.length()];
                    //create file stream
                    FileInputStream fis = new FileInputStream(file);
                    //create buffer
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    // read 0->length into array byte
                    bis.read(dataBytes, 0, dataBytes.length);
                    
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.file = file;
                    fileInfo.dataBytes = dataBytes;

                    Content data = new Content();
                    data.action = "send-file";
                    data.fromPort = ChatView.socket.getLocalPort();
                    data.toPort = ChatView.selectedUser;
                    data.message = "send " + file.getName();
                    data.username = user.username;
                    data.fileInfo = fileInfo;

                    objOutputStream = new ObjectOutputStream(ChatView.socket.getOutputStream());
                    objOutputStream.writeObject(ActionEnum.CLIENTSENDMESSAGE.getAction());
                    objOutputStream.writeObject(data);
                    objOutputStream.flush();
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Open choose file fail",
                        "Choose file fail", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please choose user for send file.",
                        "Choose file fail", JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btn_send_fileActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            int confirm = JOptionPane.showConfirmDialog(this, "Ban muon thoat chuong trinh ?", "Thong bao!", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                objOutputStream = new ObjectOutputStream(ChatView.socket.getOutputStream());
                objOutputStream.writeObject(ActionEnum.EXITCHAT.getAction());
                objOutputStream.writeObject(String.valueOf(ChatView.socket.getLocalPort()));
                objOutputStream.flush();
                ChatView.socket.close();
                this.dispose();
                System.exit(0);
            }
        } catch (IOException e) {
        }
    }//GEN-LAST:event_formWindowClosing

    private void btn_saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_saveMouseClicked
        try {
            String selectedName = list_file.getSelectedValue();
            if (selectedName != null) {
                FileInfo selectedFile = null;
                for (Map.Entry file: files.entrySet()) {
                    FileInfo fileInfo = (FileInfo) file.getValue();
                    String name = fileInfo.file.getName();
                    if (selectedName.equals(name)) {
                        selectedFile = (FileInfo) fileInfo;
                    }
                }
                if (selectedFile != null) {
                    JFileChooser fc = new JFileChooser();
                    // set name file into dialog
                    fc.setSelectedFile(selectedFile.file);
                    //opend dialog
                    int returnVal = fc.showSaveDialog(openFileChooser);
                    if (returnVal == JFileChooser.APPROVE_OPTION) { // clicked save on dialog
                        try {
                            //create file stream with path get from dialog fc.getSelectedFile()
                            FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                            //create buffer
                            BufferedOutputStream bos = new BufferedOutputStream(fos);
                            //save file
                            bos.write(selectedFile.dataBytes);
                            bos.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please choose file need save",
                            "Choose file", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please choose file need save.",
                            "Choose file", JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException e) {
        }
    }//GEN-LAST:event_btn_saveMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ChatView(user).setVisible(true);
                } catch (IOException ex) {
                    //
                }
            }
        });
        
//        loadChat();
    }
    
    public static String loadActive(DefaultListModel defaultListModel) {
        try {
            ObjectInputStream objInputStream = new ObjectInputStream(ChatView.socket.getInputStream());
            Object receiverAction = objInputStream.readObject(); //#message 1
            if (receiverAction != null) {
                //setup users active
                String action = String.valueOf(receiverAction);
                if (action.equals(ActionEnum.UPDATEACTIVES.getAction()) || 
                    action.equals(ActionEnum.FIRSTCALL.getAction()) ||
                    action.equals(ActionEnum.EXITCHAT.getAction())) 
                {
                    // set list active from server
                    ChatView.listActive = (HashMap<Integer, String>) objInputStream.readObject(); //#message 2
                
                    defaultListModel.removeAllElements();
                    for (Map.Entry userActive: listActive.entrySet()) {
                        if (!userActive.getKey().equals(ChatView.socket.getLocalPort())) {
                            defaultListModel.addElement(String.valueOf(userActive.getKey()) + "#" + userActive.getValue());
                        }
                    }
                    list_user.setModel(defaultListModel);
                    
                    if (action.equals(ActionEnum.UPDATEACTIVES.getAction())) {
                        return ActionEnum.UPDATEACTIVES.getAction();
                    }
                    if (action.equals(ActionEnum.FIRSTCALL.getAction())) {
                        return ActionEnum.FIRSTCALL.getAction();
                    }
                    if (action.equals(ActionEnum.EXITCHAT.getAction())) {
                        return ActionEnum.EXITCHAT.getAction();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            
        }
        return "";
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_send;
    private javax.swing.JButton btn_send_file;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> list_file;
    private static javax.swing.JList<String> list_user;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JTextField txt_chat;
    private javax.swing.JLabel username;
    private static javax.swing.JTextArea view_chat;
    // End of variables declaration//GEN-END:variables
}
