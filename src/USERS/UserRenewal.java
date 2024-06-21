/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package USERS;

import Config.DbConnector;
import Config.Session;
import Myapps.LoginForm;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author AJ
 */
public class UserRenewal extends javax.swing.JFrame {

    /**
     * Creates new form LoginForm
     */ private Session sn;
    public UserRenewal() {
        initComponents();
        sdate.setDate(new Date());
           updateEndDateBasedOnSubtype();
        displayData();
        sn = Session.getInstance();
        dt();
        times();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.now();
    }
      boolean checkadd = true;
    public void displayData() {
    try {
        DbConnector dbc = new DbConnector();
        // Query to select only subscribers with a pending status
        String query = "SELECT * FROM tbl_sub WHERE s_status = 'expired'";
        ResultSet rs = dbc.getData(query);
        subTable.setModel(DbUtils.resultSetToTableModel(rs));
        rs.close();
        dbc.connect.close();  // Ensure the connection is closed after use
    } catch (SQLException ex) {
        System.out.println("Errors: " + ex.getMessage());
    }
}
    public void dt(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        
        String dd = sdf.format(d);
        date.setText(dd);
    }
    
    Timer t;
    SimpleDateFormat st;
    public void times(){
        
        
        t = new Timer(0, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //thow new UnsupportedOperationException("Not supported yet");
            Date dt = new Date();
            st = new SimpleDateFormat("hh:mm:ss a");
            String tt =st.format(dt);
            time.setText(tt);
            }
        });
        t.start();
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
    public int getHeightFromWidth(int width, String imagePath) {
    if (imagePath == null || imagePath.isEmpty()) {
        System.out.println("Debug: Image path is null or empty in getHeightFromWidth");
        return 0; // or some default height
    }

    ImageIcon icon = new ImageIcon(imagePath);
    int originalWidth = icon.getIconWidth();
    int originalHeight = icon.getIconHeight();
    
    if (originalWidth == 0) {
        System.out.println("Debug: Original width is zero in getHeightFromWidth");
        return 0; // Avoid division by zero
    }

    return (width * originalHeight) / originalWidth;
}

    
  public ImageIcon ResizeImage(String imagePath, Integer width, JLabel imageLabel) {
    if (imagePath == null || imageLabel == null) {
        System.out.println("Debug: imagePath or imageLabel is null");
        return null;
    }

    ImageIcon originalIcon = new ImageIcon(imagePath);
    if (originalIcon.getIconWidth() <= 0 || originalIcon.getIconHeight() <= 0) {
        System.out.println("Debug: originalIcon is invalid or image not found at path: " + imagePath);
        return null;
    }

    int newWidth = (width != null) ? width : imageLabel.getWidth();
    int newHeight = getHeightFromWidth(newWidth, originalIcon.getIconWidth(), originalIcon.getIconHeight());

    Image img = originalIcon.getImage();
    Image resizedImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

    return new ImageIcon(resizedImg);
}

private int getHeightFromWidth(int newWidth, int originalWidth, int originalHeight) {
    if (originalWidth == 0) {
        throw new IllegalArgumentException("Original width cannot be zero");
    }
    return (newWidth * originalHeight) / originalWidth;
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

private boolean userExists(Connection connect, int userId) throws SQLException {
        String checkUserSql = "SELECT COUNT(*) FROM tbl_user WHERE user_id = ?";
        try ( PreparedStatement pst = connect.prepareStatement(checkUserSql)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    private void clearFields() {
        sid.setText("");
        sfname.setText("");
        slname.setText("");
        sdate.setDate(null);
        edate.setDate(null);
        image.setIcon(null);
        subtype.setSelectedIndex(0);
        sstatus.setSelectedIndex(0);
        checkadd = false;
    }
    public void updateEndDateBasedOnSubtype() {
        Date startDate = sdate.getDate();
        if (startDate == null) {
            startDate = new Date();  // Use current date if start date is not set
            sdate.setDate(startDate);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        String selectedSubtype = subtype.getSelectedItem().toString();

        switch (selectedSubtype.toLowerCase()) {
            case "daily":
                cal.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case "weekly":
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case "monthly":
                cal.add(Calendar.MONTH, 1);
                break;
            case "yearly":
                cal.add(Calendar.YEAR, 1);
                break;
            default:
                // Handle other cases or leave the end date unchanged
                break;
        }

        edate.setDate(cal.getTime());
    }
 
private boolean isDuplicate(String firstName, String lastName) throws SQLException {
    DbConnector dbc = new DbConnector();
    String sql = "SELECT COUNT(*) FROM tbl_sub WHERE s_fname = ? AND s_lname = ?";
    try (PreparedStatement pst = dbc.connect.prepareStatement(sql)) {
        pst.setString(1, firstName);
        pst.setString(2, lastName);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0;
        }
    }
    return false;
}
    
   
       
    
        Color sidecolor = new Color(102,0,0);
        Color headcolor = new Color(255,0,0);
        Color bodycolor = new Color(153,0,0);

   

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
        acc_user = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        add = new javax.swing.JLabel();
        clear = new javax.swing.JLabel();
        update = new javax.swing.JLabel();
        renew = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        subTable = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        sid = new javax.swing.JTextField();
        uid = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        sfname = new javax.swing.JTextField();
        slname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        sstatus = new javax.swing.JComboBox<>();
        subtype = new javax.swing.JComboBox<>();
        select = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        sdate = new com.toedter.calendar.JDateChooser();
        edate = new com.toedter.calendar.JDateChooser();
        renewal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 204, 255));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        acc_user.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        acc_user.setForeground(new java.awt.Color(255, 255, 255));
        acc_user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(acc_user, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 120, 30));

        logout.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        logout.setForeground(new java.awt.Color(51, 51, 51));
        logout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-logout-24.png"))); // NOI18N
        logout.setText(" Logout");
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
        });
        jPanel2.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 470, 160, 40));

        add.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        add.setForeground(new java.awt.Color(255, 255, 255));
        add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        add.setText("    ADD");
        add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add.setEnabled(false);
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
        });
        jPanel2.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 110, 40));

        clear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        clear.setForeground(new java.awt.Color(255, 255, 255));
        clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        clear.setText("   CLEAR");
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.setEnabled(false);
        clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearMouseClicked(evt);
            }
        });
        jPanel2.add(clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 120, 40));

        update.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        update.setForeground(new java.awt.Color(255, 255, 255));
        update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        update.setText("   UPDATE");
        update.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        update.setEnabled(false);
        update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateMouseClicked(evt);
            }
        });
        jPanel2.add(update, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 120, 40));

        renew.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        renew.setForeground(new java.awt.Color(51, 51, 51));
        renew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        renew.setText("   RENEWAL");
        renew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        renew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                renewMouseClicked(evt);
            }
        });
        jPanel2.add(renew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 120, 40));

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 60, 160, 550);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("USER'S SUBSCRIPTIONS");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 370, 40));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-arrow-back-24 (1).png"))); // NOI18N
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 0, 30, 40));
        jPanel3.add(time, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 80, 30));
        jPanel3.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 30));

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 0, 1000, 60);

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        image.setText("                                   No Image Inserted");
        jPanel6.add(image, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 250));

        jPanel1.add(jPanel6);
        jPanel6.setBounds(680, 70, 300, 270);

        subTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(subTable);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(170, 70, 500, 370);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel13.setText("Subs ID          :");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(180, 450, 110, 30);

        sid.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sid.setForeground(new java.awt.Color(204, 0, 0));
        sid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        sid.setEnabled(false);
        jPanel1.add(sid);
        sid.setBounds(300, 450, 220, 30);

        uid.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        uid.setForeground(new java.awt.Color(204, 0, 0));
        uid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        uid.setEnabled(false);
        uid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uidActionPerformed(evt);
            }
        });
        jPanel1.add(uid);
        uid.setBounds(300, 490, 220, 30);

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Users ID         :");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(180, 490, 110, 30);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Firstname         :");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(180, 530, 110, 30);

        sfname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sfname.setForeground(new java.awt.Color(204, 0, 0));
        sfname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        sfname.setEnabled(false);
        jPanel1.add(sfname);
        sfname.setBounds(300, 530, 220, 30);

        slname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        slname.setForeground(new java.awt.Color(204, 0, 0));
        slname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        slname.setEnabled(false);
        jPanel1.add(slname);
        slname.setBounds(300, 570, 220, 30);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Lastname         :");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(180, 570, 110, 30);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Start  Date        :");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(570, 450, 110, 30);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("End Date         :");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(570, 490, 110, 30);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Sub Type         :");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(570, 530, 110, 30);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Status         :");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(570, 570, 110, 30);

        sstatus.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        sstatus.setForeground(new java.awt.Color(204, 0, 0));
        sstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Expired", "Pending" }));
        sstatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        sstatus.setEnabled(false);
        sstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sstatusActionPerformed(evt);
            }
        });
        jPanel1.add(sstatus);
        sstatus.setBounds(690, 570, 220, 30);

        subtype.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        subtype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", "Yearly" }));
        subtype.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        subtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subtypeActionPerformed(evt);
            }
        });
        jPanel1.add(subtype);
        subtype.setBounds(690, 530, 220, 30);

        select.setText("SELECT");
        select.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        jPanel1.add(select);
        select.setBounds(730, 360, 80, 30);

        remove.setText("REMOVE");
        remove.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        remove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeMouseClicked(evt);
            }
        });
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        jPanel1.add(remove);
        remove.setBounds(860, 360, 73, 30);

        sdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        sdate.setEnabled(false);
        jPanel1.add(sdate);
        sdate.setBounds(690, 450, 220, 30);

        edate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        edate.setEnabled(false);
        jPanel1.add(edate);
        edate.setBounds(690, 490, 220, 30);

        renewal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        renewal.setText("RENEW");
        renewal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                renewalMouseClicked(evt);
            }
        });
        jPanel1.add(renewal);
        renewal.setBounds(680, 403, 110, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 997, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
  
Session sn = Session.getInstance();  
        if(sn.getUid()==0){
            JOptionPane.showMessageDialog(null,"No Account, Login Frist","Warning", JOptionPane.WARNING_MESSAGE);
            LoginForm lf = new LoginForm();
            lf.setVisible(true);
            this.dispose();
        }else{
            acc_user.setText(" "+sn.getFname()+" "+sn.getLname());
        } 
    }//GEN-LAST:event_formWindowActivated

     public static boolean loginAcc(String username, String password){
        DbConnector connector = new DbConnector();
        try{
            String query = "SELECT * FROM tbl_user  WHERE username = '" + username + "' AND password = '" + password + "'";
            ResultSet resultSet = connector.getData(query);
            return resultSet.next();
        }catch (SQLException ex) {
            return false;
        }

    }

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        int a = JOptionPane.showConfirmDialog(null,"Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        LoginForm uf = new LoginForm();
        uf.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logoutMouseClicked

    
    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        UserDash ud = new UserDash();
        ud.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel11MouseClicked

    private void subTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subTableMouseClicked
      int rowIndex = subTable.getSelectedRow();
if (rowIndex < 0) {
    JOptionPane.showMessageDialog(null, "Please select an Item", "Warning", JOptionPane.WARNING_MESSAGE);
} else {
    DbConnector dbc = null;
    ResultSet rs = null;
    try {
        dbc = new DbConnector();
        TableModel tbl = subTable.getModel();
        String sId = tbl.getValueAt(rowIndex, 0).toString();

        if (sId == null || sId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT * FROM tbl_sub WHERE s_id = ? AND s_status = 'expired'";
        PreparedStatement pst = dbc.connect.prepareStatement(query);
        pst.setString(1, sId);
        rs = pst.executeQuery();

        if (rs.next()) {
            sid.setText(rs.getString("s_id"));
            uid.setText(rs.getString("u_id"));
            sfname.setText(rs.getString("s_fname"));
            slname.setText(rs.getString("s_lname"));

            java.sql.Date sqlSDate = rs.getDate("s_sdate");
            java.sql.Date sqlEDate = rs.getDate("s_edate");
            if (sqlSDate != null) {
                sdate.setDate(new java.util.Date(sqlSDate.getTime()));
            }
            if (sqlEDate != null) {
                edate.setDate(new java.util.Date(sqlEDate.getTime()));
            }

            String imagePath = rs.getString("s_image");
            System.out.println("Debug: Image path from DB = " + imagePath);

            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon resizedImage = ResizeImage(imagePath, null, image);
                if (resizedImage != null) {
                    image.setIcon(resizedImage);
                } else {
                    System.out.println("Debug: Resized image is null");
                    image.setIcon(null);  // Ensure previous image is cleared if resizing fails
                }
            } else {
                System.out.println("Debug: Image path is null or empty");
                image.setIcon(null);  // Clear image display if no image path is found
            }

            subtype.setSelectedItem(rs.getString("s_subtype"));
            sstatus.setSelectedItem(rs.getString("s_status"));
            add.setEnabled(false);

            checkadd = false;
        } else {
            JOptionPane.showMessageDialog(null, "No data found for the selected item.", "Info", JOptionPane.INFORMATION_MESSAGE);
            image.setIcon(null);  // Clear image display if no data is found
        }
    } catch (SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
    } finally {
        // Ensure resources are closed to avoid memory leaks
        try {
            if (rs != null) rs.close();
            if (dbc != null && dbc.connect != null) dbc.connect.close();
        } catch (SQLException ex) {
            System.out.println("Error closing resources: " + ex.getMessage());
        }
    }
}
    }//GEN-LAST:event_subTableMouseClicked

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

    private void removeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeMouseClicked

    }//GEN-LAST:event_removeMouseClicked

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        remove.setEnabled(false);
        select.setEnabled(true);
        image.setIcon(null);
        destination = "";
        path = "";
    }//GEN-LAST:event_removeActionPerformed

    private void sstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sstatusActionPerformed

    private void uidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_uidActionPerformed

    private void subtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subtypeActionPerformed
         updateEndDateBasedOnSubtype();
    }//GEN-LAST:event_subtypeActionPerformed

    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
             try {
        Session sn = Session.getInstance();  // Ensure Session instance is properly initialized
        
        if (checkadd) {
            // Check if any of the required fields are empty
            if (sfname.getText().isEmpty() || slname.getText().isEmpty() || sdate.getDate() == null || edate.getDate() == null) {
                JOptionPane.showMessageDialog(null, "All fields are required!");
            } else {
                DbConnector dbc = new DbConnector();
                String sql = "INSERT INTO tbl_sub (u_id, s_fname, s_lname, s_sdate, s_edate, s_status, s_subtype, s_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement pst = dbc.connect.prepareStatement(sql)) {
                    int userId = sn.getUid();
                    
                    // Verify that the userId exists in tbl_user
                    if (!userExists(dbc.connect, userId)) {
                        JOptionPane.showMessageDialog(null, "User ID does not exist: " + userId);
                        return;
                    }
                    
                    String firstName = sfname.getText().trim();
                    String lastName = slname.getText().trim();
                    java.util.Date utilsDate = sdate.getDate();
                    java.util.Date utileDate = edate.getDate();
                    java.sql.Date sqlsDate = new java.sql.Date(utilsDate.getTime());
                    java.sql.Date sqleDate = new java.sql.Date(utileDate.getTime());
                    
                    // Check for duplicates before insertion
                    if (isDuplicate(firstName, lastName)) {
                        JOptionPane.showMessageDialog(null, "Subscriber with the same first name and last name already exists. Cannot add.");
                        return;
                    }
                    
                    pst.setInt(1, userId);
                    pst.setString(2, firstName);
                    pst.setString(3, lastName);
                    pst.setDate(4, sqlsDate);
                    pst.setDate(5, sqleDate);
                    pst.setString(6, sstatus.getSelectedItem().toString());
                    pst.setString(7, subtype.getSelectedItem().toString());
                    pst.setString(8, destination);
                    
                    pst.executeUpdate();
                    
                    // Copy image file if selected
                    if (selectedFile != null && destination != null) {
                        try {
                            Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {
                            System.out.println("Insert Image Error: " + ex);
                        }
                    } else {
                        System.out.println("No image found!");
                    }
                    
                    JOptionPane.showMessageDialog(null, "Successfully Added!");
                    displayData();
                    clearFields();
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Clear The Field First");
        }
        
    } catch (NullPointerException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "NullPointerException: " + ex.getMessage());
    }
    }//GEN-LAST:event_addMouseClicked

    private void clearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearMouseClicked
       checkadd = true;
        
                 sid.setText("");
                 uid.setText("");
                 sfname.setText("");
                 slname.setText("");
                sdate.setDate(new Date());
                 edate.setDate(null);
                 image.setIcon(null);
                  subtype.setSelectedIndex(0);
                sstatus.setSelectedIndex(0);
    }//GEN-LAST:event_clearMouseClicked

    private void updateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateMouseClicked
    if (sid.getText().isEmpty()) {
    JOptionPane.showMessageDialog(null, "Please Select a Subscriber First");
} else {
    if (sfname.getText().isEmpty() || slname.getText().isEmpty() || sdate.getDate() == null || edate.getDate() == null) {
        JOptionPane.showMessageDialog(null, "All fields are required!");

    } else {
        System.out.println("Debug: All fields are filled, proceeding with update");

        DbConnector dbc = new DbConnector();
        String sql = "UPDATE tbl_sub SET s_fname = ?, s_lname = ?, s_sdate = ?, s_edate = ?, s_status = ?, s_subtype = ?, s_image = ? WHERE s_id = ?";

        try (
             PreparedStatement pst = dbc.connect.prepareStatement(sql);) {

            System.out.println("Debug: Prepared Statement created");
 String firstName = sfname.getText().trim();
            String lastName = slname.getText().trim();
java.util.Date utilsDate = sdate.getDate();
                    java.util.Date utileDate = edate.getDate();
        java.sql.Date sqlsDate = new java.sql.Date(utilsDate.getTime());
             java.sql.Date sqleDate = new java.sql.Date(utileDate.getTime());  
             if (isDuplicate(firstName, lastName)) {
                JOptionPane.showMessageDialog(null, "Subscriber with the same first name and last name already exists. Cannot add.");
                return;
            }
            pst.setString(1, sfname.getText().trim());
            pst.setString(2, slname.getText().trim());
            pst.setDate(3, sqlsDate);
            pst.setDate(4, sqleDate);
            pst.setString(5, sstatus.getSelectedItem().toString());
            pst.setString(6, subtype.getSelectedItem().toString());
            pst.setString(7, destination);
            pst.setInt(8, Integer.parseInt(sid.getText().trim()));

            System.out.println("Debug: Executing update");

            pst.executeUpdate();

            System.out.println("Debug: Update executed");

            if (selectedFile != null && destination != null) {
                try {
                    Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    System.out.println("Insert Image Error: " + ex);
                }
            } else {
                System.out.println("No image found!");
            }

            JOptionPane.showMessageDialog(null, "Updated Successfully!");
            displayData();

           clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }
    }
}                  
    }//GEN-LAST:event_updateMouseClicked

    private void renewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renewMouseClicked
        UserRenewal af = new UserRenewal();
        af.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_renewMouseClicked

    private void renewalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renewalMouseClicked
   if (sid.getText().isEmpty()) {
    JOptionPane.showMessageDialog(null, "Please Select a Subscriber First");
} else {
    if (sfname.getText().isEmpty() || slname.getText().isEmpty() || sdate.getDate() == null || edate.getDate() == null) {
        JOptionPane.showMessageDialog(null, "All fields are required!");
    } else {
        // Automatically set status to "pending"
        String status = "Pending";

        System.out.println("Debug: All fields are filled, proceeding with update");

        DbConnector dbc = new DbConnector();
        String sql = "UPDATE tbl_sub SET s_fname = ?, s_lname = ?, s_sdate = ?, s_edate = ?, s_status = ?, s_subtype = ?, s_image = ? WHERE s_id = ?";

        try (PreparedStatement pst = dbc.connect.prepareStatement(sql)) {
            System.out.println("Debug: Prepared Statement created");

            String firstName = sfname.getText().trim();
            String lastName = slname.getText().trim();
            java.util.Date utilsDate = sdate.getDate();
            java.util.Date utileDate = edate.getDate();
            java.sql.Date sqlsDate = new java.sql.Date(utilsDate.getTime());
            java.sql.Date sqleDate = new java.sql.Date(utileDate.getTime());

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setDate(3, sqlsDate);
            pst.setDate(4, sqleDate);
            pst.setString(5, status);
            pst.setString(6, subtype.getSelectedItem().toString().trim());
            pst.setString(7, destination);
            pst.setInt(8, Integer.parseInt(sid.getText().trim()));

            System.out.println("Debug: Executing update");
            pst.executeUpdate();
            System.out.println("Debug: Update executed");

            if (selectedFile != null && destination != null) {
                try {
                    Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    System.out.println("Insert Image Error: " + ex);
                }
            } else {
                System.out.println("No image found!");
            }

            JOptionPane.showMessageDialog(null, "Please wait for the Administator's approval!");
            displayData();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }
    }
}


    }//GEN-LAST:event_renewalMouseClicked

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
            java.util.logging.Logger.getLogger(UserRenewal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserRenewal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserRenewal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserRenewal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new UserRenewal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel acc_user;
    private javax.swing.JLabel add;
    private javax.swing.JLabel clear;
    private javax.swing.JLabel date;
    public com.toedter.calendar.JDateChooser edate;
    public javax.swing.JLabel image;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel logout;
    public javax.swing.JButton remove;
    private javax.swing.JLabel renew;
    private javax.swing.JButton renewal;
    public com.toedter.calendar.JDateChooser sdate;
    public javax.swing.JButton select;
    public javax.swing.JTextField sfname;
    public javax.swing.JTextField sid;
    public javax.swing.JTextField slname;
    public javax.swing.JComboBox<String> sstatus;
    public javax.swing.JTable subTable;
    public javax.swing.JComboBox<String> subtype;
    private javax.swing.JLabel time;
    public javax.swing.JTextField uid;
    private javax.swing.JLabel update;
    // End of variables declaration//GEN-END:variables
}
