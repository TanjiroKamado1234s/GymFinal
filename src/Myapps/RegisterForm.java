/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Myapps;

import Config.DbConnector;
import Config.PasswordHasher;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author AJ
 */
public class RegisterForm extends javax.swing.JFrame {

    /**
     * Creates new form LoginForm
     */
    public RegisterForm() {
        initComponents();
        DuplicateCheck();
    }
    public static String emails,usname;
    
    public boolean DuplicateCheck(){
        DbConnector dbc = new DbConnector();
        
        try{
            String query = "SELECT * FROM tbl_user  WHERE username = '" + uname.getText() + "' AND user_email = '" + email.getText() + "'";
            ResultSet resultSet = dbc.getData(query);
            
            if(resultSet.next()){
                emails=resultSet.getString("user_email");
                System.out.println(""+emails);
                if(emails.equals(email.getText())){
                    JOptionPane.showMessageDialog(null, "Email is Already Used","Enter Another Email", JOptionPane.WARNING_MESSAGE);
                    email.setText("");
                }
                usname=resultSet.getString("username");
                if(usname.equals(uname.getText())){
                    JOptionPane.showMessageDialog(null, "Username is Already Used","Enter Another Username", JOptionPane.WARNING_MESSAGE);
                    uname.setText("");
                }
                return true;
            }else{
                return false;
            }
        }catch(SQLException ex){
            System.out.println(""+ex);
            return false;
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lname = new javax.swing.JTextField();
        cancel = new javax.swing.JButton();
        register = new javax.swing.JButton();
        fname = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        uname = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        acctype = new javax.swing.JComboBox<>();
        pass = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(0, 204, 255));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("REGISTER FORM");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("X");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 400, 40);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Firstname");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(50, 60, 80, 20);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Lastname ");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(50, 110, 80, 20);

        lname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lname.setForeground(new java.awt.Color(204, 0, 0));
        lname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel1.add(lname);
        lname.setBounds(50, 130, 280, 30);

        cancel.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        cancel.setText("Cancel");
        cancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelMouseClicked(evt);
            }
        });
        jPanel1.add(cancel);
        cancel.setBounds(110, 420, 80, 30);

        register.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        register.setText("Register");
        register.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        register.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registerMouseClicked(evt);
            }
        });
        register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerActionPerformed(evt);
            }
        });
        jPanel1.add(register);
        register.setBounds(210, 420, 80, 30);

        fname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        fname.setForeground(new java.awt.Color(204, 0, 0));
        fname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel1.add(fname);
        fname.setBounds(50, 80, 280, 30);

        email.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        email.setForeground(new java.awt.Color(204, 0, 0));
        email.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel1.add(email);
        email.setBounds(50, 190, 280, 30);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Email");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(50, 170, 80, 20);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Account Type");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(50, 350, 80, 20);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Username");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(50, 230, 80, 20);

        uname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        uname.setForeground(new java.awt.Color(204, 0, 0));
        uname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel1.add(uname);
        uname.setBounds(50, 250, 280, 30);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Password ");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(50, 290, 80, 20);

        acctype.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        acctype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manager", "User" }));
        acctype.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        acctype.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.add(acctype);
        acctype.setBounds(50, 370, 280, 30);

        pass.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        pass.setForeground(new java.awt.Color(204, 0, 0));
        pass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        jPanel1.add(pass);
        pass.setBounds(50, 310, 280, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_registerActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if(a == JOptionPane.YES_OPTION){
                  System.exit(0);     
            }
    }//GEN-LAST:event_jLabel5MouseClicked

    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
        LoginForm lf = new LoginForm();
        lf.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_cancelMouseClicked

    private void registerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registerMouseClicked
        
        if(fname.getText().isEmpty()||lname.getText().isEmpty()||email.getText().isEmpty()
                ||uname.getText().isEmpty()||pass.getText().isEmpty()){
               JOptionPane.showMessageDialog(null, "All fields are required","Warning", JOptionPane.WARNING_MESSAGE);
        }else if(pass.getText().length()<8){
               JOptionPane.showMessageDialog(null, "Password character should be 8 above","Warning", JOptionPane.WARNING_MESSAGE);
               pass.setText("");
        }else if(DuplicateCheck()){
            System.out.println("Duplicate Exist!");
        }else{
               DbConnector dbc = new DbConnector();
           
              try{
                  String ps = PasswordHasher.hashPassword(pass.getText());

            if(dbc.insertData("INSERT INTO tbl_user (user_firstname, user_lastname, user_email, username, password, user_account, user_status, user_image) "
                    + "VALUES ('"+fname.getText()+"', '"+lname.getText()+"', '"+email.getText()+"', '"+uname.getText()+"','"+ps+"', '"+acctype.getSelectedItem()+"', 'Pending','')"))
            {                                         
                JOptionPane.showMessageDialog(null, "Registration Success","Succes", JOptionPane.INFORMATION_MESSAGE);
                LoginForm ad = new LoginForm();
                ad.setVisible(true);
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Connection Error","Error", JOptionPane.ERROR_MESSAGE);
           }
           }catch (NoSuchAlgorithmException ex) {
            System.out.println(""+ex);
            }
        }
    }//GEN-LAST:event_registerMouseClicked

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passActionPerformed
    
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
            java.util.logging.Logger.getLogger(RegisterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegisterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegisterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> acctype;
    private javax.swing.JButton cancel;
    private javax.swing.JTextField email;
    private javax.swing.JTextField fname;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField lname;
    private javax.swing.JPasswordField pass;
    private javax.swing.JButton register;
    private javax.swing.JTextField uname;
    // End of variables declaration//GEN-END:variables
}