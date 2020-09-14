/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.view;

import chatapp.model.User;
import chatapp.common.Client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Dell
 */
public class ChatView extends javax.swing.JFrame {

    /**
     * Creates new form ChatView
     */
    public static User user;
    public int serverPort = 12345;
    public Socket socket;
    public static Client client;
    public int clientPortId;
    public static HashMap listTextChat;
    public static String prevPortSelected = "";
    
    public ChatView(User user) throws IOException {
        initComponents();
        // create socket client
        Socket _socket = new Socket(InetAddress.getLocalHost(), serverPort);
        Client _client = new Client(_socket);
        HashMap<String, String> _listTextChat = new HashMap<String, String>();
        
        //first call
        _client.read();
        
        this.user = user;
        this.socket = _socket;
        this.client = _client;
        this.clientPortId = _socket.getLocalPort();
        this.listTextChat = _listTextChat;
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btn_load_active, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_send, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_load_chat)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_load_active)
                    .addComponent(btn_load_chat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
        String selected = selected = list_user.getSelectedValue();
        if (selected != null) {
            StringTokenizer strToken = new StringTokenizer(selected , "#");
            strToken.nextToken();
            String portUser = strToken.nextToken();
//            if (!prevPortSelected.equals("")) {
//                String fullText = view_chat.getText();
//                listTextChat.remove(prevPortSelected);
//                listTextChat.put(prevPortSelected, fullText);
//            }
            if (!portUser.equals("")) {
                String val = (String) listTextChat.get(portUser);
                view_chat.setText(val);
            }
        }
    }//GEN-LAST:event_list_userMouseClicked

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        try {
            String textChat = txt_chat.getText();
            String selected = selected = list_user.getSelectedValue();
            if (!textChat.equals("") && selected != null) {
                StringTokenizer strToken = new StringTokenizer(selected , "#");
                //
                strToken.nextToken();
                String portReciver = strToken.nextToken();
                System.out.println("client > portReciver: " + portReciver);
                //
                String message = textChat + "#" + user.username + "#" + portReciver;
                client.send(message);
            }
        } catch (IOException e) {
            //
        }
        
    }//GEN-LAST:event_btn_sendActionPerformed

    private void btn_load_activeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_load_activeActionPerformed
        try {
            client.send("load@Active#");
            Thread.sleep(300);
            String listPostId = client.read();
            if (listPostId != null) {
                StringTokenizer strToken = new StringTokenizer(listPostId , "#");
                ArrayList<String> list = new ArrayList();
                while(strToken.hasMoreTokens()) {
                    list.add(strToken.nextToken());
                }
                DefaultListModel defaultListModel = new DefaultListModel();
                defaultListModel.removeAllElements();
                if (list.get(0).equals("list@Active")) {
                    int i = 1;
                    while(i < list.size()) {
                        defaultListModel.addElement("User#" + list.get(i));
                        i++;
                    }
                }
                list_user.setModel(defaultListModel);
            }
        } catch (IOException ex) {
            //
        } catch (InterruptedException ex) {
            //
        }
        
    }//GEN-LAST:event_btn_load_activeActionPerformed

    private void btn_load_chatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_load_chatActionPerformed
        try {
            this.loadChat();
        } catch (IOException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_load_chatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException, InterruptedException {
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
        
        loadChat();
    }
    
    public static void loadChat() throws IOException, InterruptedException {
        while(true) {
            String listen = client.read();
            if (listen != null) {
                StringTokenizer strToken = new StringTokenizer(listen , "#");
                String key = strToken.nextToken();
                if (!key.equals("list@Active") && key != null) {
                    String key1 = strToken.nextToken();
                    String mess = strToken.nextToken();
                    System.out.println("append: " + mess);
                    
                    if (Integer.parseInt(key) != clientPortId) {
                    
                    }
                    if (key1 != localPort) {
                    
                    }
                    String oldChat = (String) listTextChat.get(key);
                    listTextChat.remove(key);
                    listTextChat.put(key, oldChat + mess + "\n");
                    break;
                }
            }
            System.out.println("append:");
            Thread.sleep(300);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_load_active;
    private javax.swing.JButton btn_load_chat;
    private javax.swing.JButton btn_send;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> list_user;
    private javax.swing.JTextField txt_chat;
    private static javax.swing.JTextArea view_chat;
    // End of variables declaration//GEN-END:variables
}
