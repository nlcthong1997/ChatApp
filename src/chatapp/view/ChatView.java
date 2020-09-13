/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import chatapp.enumApp.ActionEnum;
import chatapp.model.ChatContent;
import chatapp.model.User;

/**
 *
 * @author Dell
 */
public class ChatView extends javax.swing.JFrame implements Runnable {

	/**
	 * Creates new form ChatView
	 */
	public static User userCurrent;
	private static Socket socketCurrent;
	ObjectInputStream objectInputStream = null;
	ObjectOutputStream objectOutputStream = null;
	List<User> lsUser = new ArrayList<User>();
	Map<User, String> userMapChat = new HashMap<User, String>();

	private boolean isStop = false;

	public ChatView(Socket socker, User user) throws IOException {
		this.userCurrent = user;
		this.socketCurrent = socker;
		initComponents();

		// send user to server
		OutputStream outputStream = socketCurrent.getOutputStream();
		// create an object output stream from the output stream so we can send an
		// object through it
		objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(user);
		objectOutputStream.flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			if (socketCurrent != null) {
				// get the input stream from the connected socket
				InputStream inputStream = socketCurrent.getInputStream();
				Object msg = "";
				while (true) {
					// create a DataInputStream so we can read data from it.
					objectInputStream = new ObjectInputStream(inputStream);
					msg = objectInputStream.readObject();
					String action = String.valueOf(msg);
					if (ActionEnum.UPDATELISTUSER.getAction().equals(action)) {
						final List<User> lsUserTemp = (List<User>) objectInputStream.readObject();
						for (User user : lsUserTemp) {
							if (!lsUser.contains(user) && !userCurrent.getUsername().equals(user.username)) {
								lsUser.add(user);
							}
						}

						Iterator a = lsUser.iterator();
						while (a.hasNext()) {
							User userRemove = (User) a.next();
							if (!lsUserTemp.contains(userRemove)) {
								lsUser.remove(userRemove);
							}
						}

						JListlist_user.setModel(setUPdateList());
					} else if (ActionEnum.UPDATECHAT.getAction().equals(action)) {
						ChatContent chat = (ChatContent) objectInputStream.readObject();
						for (User user : lsUser) {
							if (chat.getSender().getUsername().equals(user.getUsername())) {
								String appendviewchat = chat.getSender().getUsername() + ": " + chat.getContentChat()
										+ "\n";
								;
								user.setChatOfUser(appendviewchat);								
								/*if (JListlist_user.getSelectedIndex() != -1
										&& lsUser.get(JListlist_user.getSelectedIndex()).getUsername()
												.equals(user.getUsername())) {*/
									view_chat.setText(user.getChatOfUser());
//								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// Do not change this because it spawn try-catch many time while running thread!
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		JListlist_user = new javax.swing.JList<String>();
		jScrollPane2 = new javax.swing.JScrollPane();
		view_chat = new javax.swing.JTextArea();
		text_chat = new javax.swing.JTextField();
		btn_send = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		JListlist_user.setModel(setUPdateList());

		JListlist_user.setSelectedIndex(0);

		JListlist_user.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				list_userMouseClicked(evt);
			}
		});
		jScrollPane1.setViewportView(JListlist_user);

		view_chat.setColumns(20);
		view_chat.setRows(5);
		jScrollPane2.setViewportView(view_chat);

		String name = this.userCurrent.getUsername() + " Send";
		btn_send.setText(name);

		btn_send.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				String text = text_chat.getText();
				User userReceiver = null;
				if (lsUser.size() == 0) {
					return;
				}

				if (JListlist_user.getSelectedIndex() == -1) {
					userReceiver = lsUser.get(0);
				} else {
					userReceiver = lsUser.get(JListlist_user.getSelectedIndex());
				}

				ChatContent chat = new ChatContent();
				chat.setContentChat(text);
				chat.setSender(userCurrent);
				chat.setReceiver(userReceiver);

				String appendviewchat = userCurrent.getUsername() + ": " + text + "\n";
				userReceiver.setChatOfUser(appendviewchat);
				view_chat.setText(userReceiver.getChatOfUser());
				try {
					// get the output stream from the socket.
					OutputStream outputStream = socketCurrent.getOutputStream();
					// create an object output stream from the output stream so we can send an
					// object through it
					objectOutputStream = new ObjectOutputStream(outputStream);
					objectOutputStream.writeObject(chat);
					objectOutputStream.flush();
					text_chat.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(text_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 232,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btn_send, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
						.addComponent(jScrollPane2))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(text_chat).addComponent(btn_send,
														javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)))
								.addComponent(jScrollPane1))
						.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void list_userMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_list_userMouseClicked

		String chat = lsUser.get(JListlist_user.getSelectedIndex()).getChatOfUser();
		view_chat.setText(chat);

	}// GEN-LAST:event_list_userMouseClicked

	DefaultListModel setUPdateList() {
		List<String> collect = lsUser.stream().map(p -> p.getUsername()).collect(Collectors.toList());

		DefaultListModel listModel = new DefaultListModel();
		for (int i = 0; i < collect.size(); i++) {
			listModel.addElement(collect.get(i));
		}
		return listModel;
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
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
		// </editor-fold>

		/* Create and display the form */

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ChatView(socketCurrent, userCurrent).setVisible(true);
				} catch (IOException ex) {
//					Logger.getLogger(ChatView.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btn_send;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JList<String> JListlist_user;
	private javax.swing.JTextArea view_chat;
	private javax.swing.JTextField text_chat;
	// End of variables declaration//GEN-END:variables
}
