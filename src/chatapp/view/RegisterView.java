/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.view;

import chatapp.controllers.RegisterController;
import chatapp.model.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author hd161
 */
public class RegisterView extends javax.swing.JFrame {

    /**
     * Creates new form RegisterView
     */
    public RegisterView() {
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

        lb_dang_ky = new javax.swing.JLabel();
        lb_tai_khoan = new javax.swing.JLabel();
        txt_tai_khoan = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txt_mat_khau = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_nhap_lai_mat_khau = new javax.swing.JTextField();
        btn_dang_ky = new javax.swing.JButton();
        lb_dang_nhap = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lb_dang_ky.setText("ĐĂNG KÝ");

        lb_tai_khoan.setText("TÀI KHOẢN");

        jLabel1.setText("MẬT KHẨU");

        txt_mat_khau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_mat_khauActionPerformed(evt);
            }
        });

        jLabel2.setText("NHẬP LẠI MẬT KHẨU");

        btn_dang_ky.setText("ĐĂNG KÝ");
        btn_dang_ky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dang_kyActionPerformed(evt);
            }
        });

        lb_dang_nhap.setText("Đăng nhập");
        lb_dang_nhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_dang_nhapMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lb_tai_khoan)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_dang_nhap)
                    .addComponent(btn_dang_ky)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lb_dang_ky)
                        .addComponent(txt_mat_khau)
                        .addComponent(txt_tai_khoan)
                        .addComponent(txt_nhap_lai_mat_khau, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lb_dang_ky)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_tai_khoan)
                    .addComponent(txt_tai_khoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_mat_khau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_nhap_lai_mat_khau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_dang_ky)
                .addGap(18, 18, 18)
                .addComponent(lb_dang_nhap)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_mat_khauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_mat_khauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_mat_khauActionPerformed

    private void lb_dang_nhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_dang_nhapMouseClicked
        this.dispose();
        LoginView login = new LoginView();
        login.setVisible(true);
    }//GEN-LAST:event_lb_dang_nhapMouseClicked

    private void btn_dang_kyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dang_kyActionPerformed
        try {
            String username = txt_tai_khoan.getText();
            String password = txt_mat_khau.getText();
            String confirmPassword = txt_nhap_lai_mat_khau.getText();
            
            if (username.equals("") || password.equals("") || confirmPassword.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter username or password",
                        "Register fail", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Confirm password incorrect",
                        "Register fail", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            RegisterController registerController = new RegisterController();
            User user = registerController.register(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(null, "Username is exists",
                    "Register fail", JOptionPane.WARNING_MESSAGE);
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
    }//GEN-LAST:event_btn_dang_kyActionPerformed

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
            java.util.logging.Logger.getLogger(RegisterView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegisterView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegisterView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_dang_ky;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lb_dang_ky;
    private javax.swing.JLabel lb_dang_nhap;
    private javax.swing.JLabel lb_tai_khoan;
    private javax.swing.JTextField txt_mat_khau;
    private javax.swing.JTextField txt_nhap_lai_mat_khau;
    private javax.swing.JTextField txt_tai_khoan;
    // End of variables declaration//GEN-END:variables
}
