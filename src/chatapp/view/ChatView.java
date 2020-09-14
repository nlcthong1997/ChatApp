/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.view;

import chatapp.actionEnum.ActionEnum;
import chatapp.model.User;
import chatapp.common.Client;
import chatapp.model.Content;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

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
    public static HashMap<Integer, String> listActive;
    public static HashMap<String, String> listChat;
    public static int selectedUser = 0;
    public ObjectInputStream objInputStream;
    public ObjectOutputStream objOutputStream;
    
    public ChatView(User _user) throws IOException {
        initComponents();
        ChatView.user = _user;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            Socket _socket = new Socket(InetAddress.getLocalHost(), serverPort);
            ChatView.socket = _socket;

            objOutputStream = new ObjectOutputStream(ChatView.socket.getOutputStream());
            Content first = new Content();
            first.action = "first";
            first.fromPort = ChatView.socket.getLocalPort();
            first.toPort = 0;
            first.message = "";
            first.username = user.username;
            
            objOutputStream.writeObject(ActionEnum.FIRSTCALL.getAction());
            objOutputStream.writeObject(first);
            objOutputStream.flush();
            
            DefaultListModel defaultListModel = new DefaultListModel();
            ChatView.listChat = new HashMap<String, String>();
            
            ChatView.loadActive(defaultListModel);
            
            while (true) {
                String action = ChatView.loadActive(defaultListModel);
                if (ActionEnum.FIRSTCALL.getAction().equals(action)) {
                    continue;
                }
                
                //setup content
                ObjectInputStream objInputContent = new ObjectInputStream(ChatView.socket.getInputStream());
                Object receiverContent = objInputContent.readObject();
                
                String actionContent = String.valueOf(receiverContent);
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
                    
                    // server send it to yourself
                    if (content.fromPort == ChatView.socket.getLocalPort()) {
                        int index = 0;
                        for (Map.Entry userActive: listActive.entrySet()) {
                            if (userActive.getKey().equals(content.toPort)) {
                                ChatView.list_user.setSelectedIndex(index);
                            }
                            index++;
                        }
                        System.out.println("current sle: " + list_user.getSelectedValue());
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
                            }
                            index++;
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
                            System.out.println("ne sle: " + list_user.getSelectedValue());
                        }
                        
                    }
                }
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        list_user = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        view_chat = new javax.swing.JTextArea();
        txt_chat = new javax.swing.JTextField();
        btn_send = new javax.swing.JButton();
        btn_load_active = new javax.swing.JButton();
        btn_load_chat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        list_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_userMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(list_user);

        view_chat.setColumns(20);
        view_chat.setRows(5);
        jScrollPane2.setViewportView(view_chat);

        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        btn_load_active.setText("Load active");
        btn_load_active.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_load_activeActionPerformed(evt);
            }
        });

        btn_load_chat.setText("Load chat");
        btn_load_chat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_load_chatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_load_active, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_send, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btn_load_chat, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_load_active)
                    .addComponent(btn_load_chat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_chat)
                            .addComponent(btn_send, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

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
            if (ChatView.listChat.containsKey(key)) {
                keyTemp = key;
            }
            if (ChatView.listChat.containsKey(reverseKey)) {
                keyTemp = reverseKey;
            }
            if (listChat.containsKey(keyTemp)) {
                String content = listChat.get(keyTemp);
                view_chat.setText(content);
            }
        }
    }//GEN-LAST:event_list_userMouseClicked

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        try {
            String message = txt_chat.getText();
            
            if (!message.equals("") && ChatView.selectedUser != 0) {
                Content data = new Content();
                data.action = "";
                data.fromPort = ChatView.socket.getLocalPort();
                data.toPort = ChatView.selectedUser;
                data.message = message;
                data.username = user.username;
                
                objOutputStream = new ObjectOutputStream(ChatView.socket.getOutputStream());
                objOutputStream.writeObject(ActionEnum.CLIENTSENDMESSAGE.getAction());
                objOutputStream.writeObject(data);
                objOutputStream.flush();
                System.out.println("send");
            }
        } catch (IOException | NumberFormatException e) {
        }
        
        
    }//GEN-LAST:event_btn_sendActionPerformed

    private void btn_load_activeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_load_activeActionPerformed
//        try {
//            client.send("load@Active#");
//            Thread.sleep(300);
//            String listPostId = client.read();
//            if (listPostId != null) {
//                StringTokenizer strToken = new StringTokenizer(listPostId , "#");
//                ArrayList<String> list = new ArrayList();
//                while(strToken.hasMoreTokens()) {
//                    list.add(strToken.nextToken());
//                }
//                DefaultListModel defaultListModel = new DefaultListModel();
//                defaultListModel.removeAllElements();
//                if (list.get(0).equals("list@Active")) {
//                    int i = 1;
//                    while(i < list.size()) {
//                        defaultListModel.addElement("User#" + list.get(i));
//                        i++;
//                    }
//                }
//                list_user.setModel(defaultListModel);
//            }
//        } catch (IOException ex) {
//            //
//        } catch (InterruptedException ex) {
//            //
//        }
        
    }//GEN-LAST:event_btn_load_activeActionPerformed

    private void btn_load_chatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_load_chatActionPerformed
//        try {
//            this.loadChat();
//        } catch (IOException ex) {
//            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_btn_load_chatActionPerformed

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
                if (action.equals(ActionEnum.UPDATEACTIVES.getAction()) || action.equals(ActionEnum.FIRSTCALL.getAction())) {
                    ChatView.listActive = (HashMap<Integer, String>) objInputStream.readObject(); //#message 2
                
                    defaultListModel.removeAllElements();
                    for (Map.Entry userActive: listActive.entrySet()) {
                        defaultListModel.addElement(String.valueOf(userActive.getKey()) + "#" + userActive.getValue());
                    }
                    list_user.setModel(defaultListModel);
                    
                    if (action.equals(ActionEnum.UPDATEACTIVES.getAction())) {
                        return ActionEnum.UPDATEACTIVES.getAction();
                    }
                    if (action.equals(ActionEnum.FIRSTCALL.getAction())) {
                        return ActionEnum.FIRSTCALL.getAction();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            
        }
        return "";
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_load_active;
    private javax.swing.JButton btn_load_chat;
    private javax.swing.JButton btn_send;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JList<String> list_user;
    private javax.swing.JTextField txt_chat;
    private static javax.swing.JTextArea view_chat;
    // End of variables declaration//GEN-END:variables
}
