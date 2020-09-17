/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.view;

import chatapp.controllers.LoginController;
import chatapp.model.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author Dell
 */
public class LoginView extends javax.swing.JFrame {

    /**
     * Creates new form login
     */
    public LoginView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb_dang_nhap = new javax.swing.JLabel();
        lb_tai_khoan = new javax.swing.JLabel();
        txt_tai_khoan = new javax.swing.JTextField();
        lb_mat_khau = new javax.swing.JLabel();
        txt_mat_khau = new javax.swing.JTextField();
        btn_dang_nhap = new javax.swing.JButton();
        lb_dang_ky = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(400, 300));
        setMinimumSize(new java.awt.Dimension(400, 300));
        setResizable(false);

        lb_dang_nhap.setText("ĐĂNG NHẬP");
        lb_dang_nhap.setName(""); // NOI18N

        lb_tai_khoan.setText("TÀI KHOẢN");
        lb_tai_khoan.setName(""); // NOI18N

        txt_tai_khoan.setName(""); // NOI18N

        lb_mat_khau.setText("MẬT KHẨU");
        lb_mat_khau.setName(""); // NOI18N

        txt_mat_khau.setName(""); // NOI18N

        btn_dang_nhap.setText("ĐĂNG NHẬP");
        btn_dang_nhap.setName(""); // NOI18N
        btn_dang_nhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dang_nhapActionPerformed(evt);
            }
        });

        lb_dang_ky.setText("Đăng ký tài khoản");
        lb_dang_ky.setName(""); // NOI18N
        lb_dang_ky.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_dang_kyMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_tai_khoan)
                    .addComponent(lb_mat_khau))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_dang_nhap)
                    .addComponent(btn_dang_nhap)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txt_tai_khoan)
                        .addComponent(txt_mat_khau, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                    .addComponent(lb_dang_ky))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(lb_dang_nhap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_tai_khoan)
                    .addComponent(txt_tai_khoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_mat_khau)
                    .addComponent(txt_mat_khau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_dang_nhap)
                .addGap(18, 18, 18)
                .addComponent(lb_dang_ky)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_dang_nhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dang_nhapActionPerformed
       try {
            String username = txt_tai_khoan.getText();
            String password = txt_mat_khau.getText();
            if (username.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter username or password",
                        "Login fail", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LoginController loginController = new LoginController();
            User user = loginController.login(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(null, "Username or password is valid",
                    "Login fail", JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.dispose();
            ChatView chat = new ChatView(user);
            Thread thread = new Thread(chat); 
            thread.start();
            chat.setVisible(true);
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btn_dang_nhapActionPerformed

    private void lb_dang_kyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_dang_kyMouseClicked
        this.dispose();
        RegisterView register = new RegisterView();
        register.setVisible(true);
    }//GEN-LAST:event_lb_dang_kyMouseClicked

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
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_dang_nhap;
    private javax.swing.JLabel lb_dang_ky;
    private javax.swing.JLabel lb_dang_nhap;
    private javax.swing.JLabel lb_mat_khau;
    private javax.swing.JLabel lb_tai_khoan;
    private javax.swing.JTextField txt_mat_khau;
    private javax.swing.JTextField txt_tai_khoan;
    // End of variables declaration//GEN-END:variables
}
