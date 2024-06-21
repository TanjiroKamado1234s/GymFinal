/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Config.DbConnector;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author AJ
 */
public class CreateForm extends javax.swing.JFrame {

    /**
     * Creates new form LoginForm
     */
    public CreateForm() {
        initComponents();
        DuplicateCheck();
    }
    
    public String destination = "";
    File selectedFile;
    public String oldpath;
    public String path;
    
    public int FileExistenceChecker(String path){
        File file = new File(path);
        String fileName = file.getName();
        
        Path filePath = Paths.get("src/images", fileName);
        boolean fileExists = Files.exists(filePath);
        
        if (fileExists) {
            return 1;
        } else {
            return 0;
        }
    
    }
    public static int getHeightFromWidth(String imagePath, int desiredWidth) {
        try {
            // Read the image file
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            
            // Get the original width and height of the image
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            
            // Calculate the new height based on the desired width and the aspect ratio
            int newHeight = (int) ((double) desiredWidth / originalWidth * originalHeight);
            
            return newHeight;
        } catch (IOException ex) {
            System.out.println("No image found!");
        }
        
        return -1;
    }

    
    public  ImageIcon ResizeImage(String ImagePath, byte[] pic, JLabel label) {
    ImageIcon MyImage = null;
        if(ImagePath !=null){
            MyImage = new ImageIcon(ImagePath);
        }else{
            MyImage = new ImageIcon(pic);
        }
        
    int newHeight = getHeightFromWidth(ImagePath, label.getWidth());

    Image img = MyImage.getImage();
    Image newImg = img.getScaledInstance(label.getWidth(), newHeight, Image.SCALE_SMOOTH);
    ImageIcon image = new ImageIcon(newImg);
    return image;
}
    public void imageUpdater(String existingFilePath, String newFilePath){
        File existingFile = new File(existingFilePath);
        if (existingFile.exists()) {
            String parentDirectory = existingFile.getParent();
            File newFile = new File(newFilePath);
            String newFileName = newFile.getName();
            File updatedFile = new File(parentDirectory, newFileName);
            existingFile.delete();
            try {
                Files.copy(newFile.toPath(), updatedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image updated successfully.");
            } catch (IOException e) {
                System.out.println("Error occurred while updating the image: "+e);
            }
        } else {
            try{
                Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch(IOException e){
                System.out.println("Error on update!");
            }
        }
   }

    
    
        Color sidecolor = new Color(102,0,0);
        Color headcolor = new Color(255,0,0);
        Color bodycolor = new Color(153,0,0);
    
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
    
        public boolean updateCheck(){
        DbConnector dbc = new DbConnector(); 
        try{
            String query = "SELECT * FROM tbl_user  WHERE (username = '"+uname.getText()+"'OR user_email ='"+email.getText()+"')AND user_id !='"+uid.getText()+"'";
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lname = new javax.swing.JTextField();
        fname = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pass = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        uname = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        acctype = new javax.swing.JComboBox<>();
        stats = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        uid = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        update = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        remove = new javax.swing.JButton();
        select = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(0, 204, 255));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CREATE ACCOUNT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(241, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(233, 233, 233))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 650, 40);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Firstname         :");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 110, 110, 30);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Lastname         :");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 150, 110, 30);

        lname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lname.setForeground(new java.awt.Color(204, 0, 0));
        lname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel1.add(lname);
        lname.setBounds(140, 150, 220, 30);

        fname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        fname.setForeground(new java.awt.Color(204, 0, 0));
        fname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel1.add(fname);
        fname.setBounds(140, 110, 220, 30);

        email.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        email.setForeground(new java.awt.Color(204, 0, 0));
        email.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        jPanel1.add(email);
        email.setBounds(140, 190, 220, 30);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Email               :");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(20, 190, 110, 30);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("User Status       :");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 350, 110, 30);

        pass.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        pass.setForeground(new java.awt.Color(204, 0, 0));
        pass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel1.add(pass);
        pass.setBounds(140, 270, 220, 30);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("Username         :");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(20, 230, 110, 30);

        uname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        uname.setForeground(new java.awt.Color(204, 0, 0));
        uname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel1.add(uname);
        uname.setBounds(140, 230, 220, 30);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Password          :");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(20, 270, 110, 30);

        acctype.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        acctype.setForeground(new java.awt.Color(204, 0, 0));
        acctype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manager", "User" }));
        acctype.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        acctype.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.add(acctype);
        acctype.setBounds(140, 310, 220, 30);

        stats.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        stats.setForeground(new java.awt.Color(204, 0, 0));
        stats.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Deactivate", "Pending" }));
        stats.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        stats.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsActionPerformed(evt);
            }
        });
        jPanel1.add(stats);
        stats.setBounds(140, 350, 220, 30);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Account Type    :");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(20, 310, 110, 30);

        uid.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        uid.setForeground(new java.awt.Color(204, 0, 0));
        uid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        uid.setEnabled(false);
        jPanel1.add(uid);
        uid.setBounds(140, 70, 220, 30);

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("User ID            :");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(20, 70, 110, 30);

        add.setBackground(new java.awt.Color(153, 153, 153));
        add.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        add.setForeground(new java.awt.Color(204, 0, 0));
        add.setText("Add");
        add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
        });
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        jPanel1.add(add);
        add.setBounds(140, 400, 80, 30);

        cancel.setBackground(new java.awt.Color(153, 153, 153));
        cancel.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        cancel.setForeground(new java.awt.Color(204, 0, 0));
        cancel.setText("Cancel");
        cancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelMouseClicked(evt);
            }
        });
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        jPanel1.add(cancel);
        cancel.setBounds(140, 440, 80, 30);

        update.setBackground(new java.awt.Color(153, 153, 153));
        update.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        update.setForeground(new java.awt.Color(204, 0, 0));
        update.setText("Update");
        update.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        update.setEnabled(false);
        update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateMouseClicked(evt);
            }
        });
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        jPanel1.add(update);
        update.setBounds(240, 400, 80, 30);

        clear.setBackground(new java.awt.Color(153, 153, 153));
        clear.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        clear.setForeground(new java.awt.Color(204, 0, 0));
        clear.setText("Clear");
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearMouseClicked(evt);
            }
        });
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });
        jPanel1.add(clear);
        clear.setBounds(240, 440, 80, 30);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(image, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(image, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(370, 70, 260, 310);

        remove.setText("REMOVE");
        remove.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        jPanel1.add(remove);
        remove.setBounds(510, 400, 73, 30);

        select.setText("SELECT");
        select.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        jPanel1.add(select);
        select.setBounds(400, 400, 69, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void statsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statsActionPerformed

    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked

        if(fname.getText().isEmpty()||lname.getText().isEmpty()||email.getText().isEmpty()
            ||uname.getText().isEmpty()||pass.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "All fields are required!","Warning", JOptionPane.WARNING_MESSAGE);
        }else if(pass.getText().length()<8){
            JOptionPane.showMessageDialog(null, "Password character should be 8 above","Warning", JOptionPane.WARNING_MESSAGE);
            pass.setText("");
        }else if(DuplicateCheck()){
            System.out.println("Duplicate Exist!");
        }

        else{
            DbConnector dbc = new DbConnector();

            if(dbc.insertData("INSERT INTO tbl_user (user_firstname, user_lastname, user_email, username, password, user_account, user_status) "
                + "VALUES ('"+fname.getText()+"', '"+lname.getText()+"', '"+email.getText()+"', '"+uname.getText()+"', '"+pass.getText()+"', '"+acctype.getSelectedItem()+"',)"))
        {
            JOptionPane.showMessageDialog(null, "Registration Success","Success", JOptionPane.INFORMATION_MESSAGE);
            ManagerForm ad = new ManagerForm();
            ad.setVisible(true);
            this.dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Connection Error","Error", JOptionPane.ERROR_MESSAGE);
        }
        }
    }//GEN-LAST:event_addMouseClicked

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        if(fname.getText().isEmpty()||lname.getText().isEmpty()||email.getText().isEmpty()
            ||uname.getText().isEmpty()||pass.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "All fields are required","Warning", JOptionPane.WARNING_MESSAGE);
        }else if(pass.getText().length()<8){
            JOptionPane.showMessageDialog(null, "Password character should be 8 above","Warning", JOptionPane.WARNING_MESSAGE);
            pass.setText("");
        }else if(updateCheck()){
            System.out.println("Update Exist!");
        }else{
            DbConnector dbc = new DbConnector();

            if(dbc.insertData("INSERT INTO tbl_user (user_firstname, user_lastname, user_email, username, password, user_account, user_status, user_image) "
                + "VALUES ('"+fname.getText()+"', '"+lname.getText()+"', '"+email.getText()+"', '"+uname.getText()+"', '"+pass.getText()+"', '"+acctype.getSelectedItem()+"', '"+stats.getSelectedItem()+"','"+destination+"')"))
        {
            try{
            Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(null, "Registration Success","Success", JOptionPane.INFORMATION_MESSAGE);
            ManagerForm ad = new ManagerForm();
            ad.setVisible(true);
            this.dispose();
            }catch(IOException ex){
                System.out.println("Insert Image Error: "+ex);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Connection Error","Error", JOptionPane.ERROR_MESSAGE);
        }
        }
    }//GEN-LAST:event_addActionPerformed

    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
        
    }//GEN-LAST:event_cancelMouseClicked

    private void updateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_updateMouseClicked

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        if(fname.getText().isEmpty()||lname.getText().isEmpty()||email.getText().isEmpty()
             ||uname.getText().isEmpty()||pass.getText().isEmpty()){
             JOptionPane.showMessageDialog(null, "All fields are required","Warning", JOptionPane.WARNING_MESSAGE);
         }else if(pass.getText().length()<8){
             JOptionPane.showMessageDialog(null, "Password character should be 8 above","Warning", JOptionPane.WARNING_MESSAGE);
             pass.setText("");
         }else if(updateCheck()){
             System.out.println("Update Exist!");
         }else{
        
       DbConnector dbc=new DbConnector();
       dbc.updateData("UPDATE tbl_user SET user_firstname='"+fname.getText()+"',user_lastname='"+lname.getText()+"',"
               +"user_email='"+email.getText()+"',username='"+uname.getText()+"',"
                   +"password='"+pass.getText()+"',user_account='"+acctype.getSelectedItem()+"',"
                       + "user_status='"+stats.getSelectedItem()+"',user_image ='"+destination+"'WHERE user_id='"+uid.getText()+"'");
            
       if(destination.isEmpty()){
           File existingFile = new File(oldpath);
           if(existingFile.exists()){
               existingFile.delete();
           }
       }else{
           if(!(oldpath.equals(path))){
               imageUpdater(oldpath, path);
       }
       
       }
       JOptionPane.showMessageDialog(null, "Update Successfully","Success", JOptionPane.INFORMATION_MESSAGE);
            ManagerForm ad = new ManagerForm();
            ad.setVisible(true);
            this.dispose();
         }
       
    }//GEN-LAST:event_updateActionPerformed

    private void clearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearMouseClicked
                 uid.setText("");
                 fname.setText("");
                 lname.setText("");
                 email.setText("");
                 uname.setText("");
                 pass.setText("");
                 image.setIcon(null);
                acctype.setSelectedIndex(0);
                stats.setSelectedIndex(0);
    }//GEN-LAST:event_clearMouseClicked

    private void cancel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancel3ActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
            ManagerForm ad = new ManagerForm();
            ad.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        remove.setEnabled(false);
        select.setEnabled(true);
        image.setIcon(null);
        destination = "";
        path = "";
    }//GEN-LAST:event_removeActionPerformed

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        selectedFile = fileChooser.getSelectedFile();
                        destination = "src/images/" + selectedFile.getName();
                        path  = selectedFile.getAbsolutePath();
                        
                        
                        if(FileExistenceChecker(path) == 1){
                          JOptionPane.showMessageDialog(null, "File Already Exist, Rename or Choose another!");
                            destination = "";
                            path="";
                        }else{
                            image.setIcon(ResizeImage(path, null, image));
                            select.setEnabled(false);
                            remove.setEnabled(true);
                            
                        }
                    } catch (Exception ex) {
                        System.out.println("File Error!");
                    }
                }
    }//GEN-LAST:event_selectActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearActionPerformed
    
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
            java.util.logging.Logger.getLogger(CreateForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox<String> acctype;
    public javax.swing.JButton add;
    private javax.swing.JButton cancel;
    private javax.swing.JButton clear;
    public javax.swing.JTextField email;
    public javax.swing.JTextField fname;
    public javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JTextField lname;
    public javax.swing.JTextField pass;
    public javax.swing.JButton remove;
    public javax.swing.JButton select;
    public javax.swing.JComboBox<String> stats;
    public javax.swing.JTextField uid;
    public javax.swing.JTextField uname;
    public javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
