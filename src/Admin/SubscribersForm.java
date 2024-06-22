/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Config.DbConnector;
import Config.Session;
import Myapps.LoginForm;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author garub
 */
public class SubscribersForm extends javax.swing.JFrame {

   
    /**
     * Creates new form SubscribersForm
     */private Session sn;
    public SubscribersForm() {
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
        String query = "SELECT s_id, u_id, s_fname, s_status FROM tbl_sub WHERE s_status IN ('Active', 'Expired')";
        ResultSet rs = dbc.getData(query);
        subTable.setModel(DbUtils.resultSetToTableModel(rs));
        rs.close();
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
        jLabel4 = new javax.swing.JLabel();
        add = new javax.swing.JLabel();
        update = new javax.swing.JLabel();
        clear = new javax.swing.JLabel();
        delete = new javax.swing.JLabel();
        acc_user = new javax.swing.JLabel();
        pendingReq = new javax.swing.JLabel();
        SubPrinting = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        subTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        sid = new javax.swing.JTextField();
        sfname = new javax.swing.JTextField();
        slname = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        select = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        sstatus = new javax.swing.JComboBox<>();
        uid = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        subtype = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        sdate = new com.toedter.calendar.JDateChooser();
        edate = new com.toedter.calendar.JDateChooser();
        time = new javax.swing.JLabel();
        date = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("MANAGER");

        add.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        add.setForeground(new java.awt.Color(255, 255, 255));
        add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        add.setText("    ADD");
        add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
        });

        update.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        update.setForeground(new java.awt.Color(255, 255, 255));
        update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        update.setText("   UPDATE");
        update.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateMouseClicked(evt);
            }
        });

        clear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        clear.setForeground(new java.awt.Color(255, 255, 255));
        clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        clear.setText("   CLEAR");
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearMouseClicked(evt);
            }
        });

        delete.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        delete.setForeground(new java.awt.Color(255, 255, 255));
        delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        delete.setText("   DELETE");
        delete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
        });

        acc_user.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        acc_user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        pendingReq.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        pendingReq.setForeground(new java.awt.Color(255, 255, 255));
        pendingReq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        pendingReq.setText("   PENDING");
        pendingReq.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pendingReq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendingReqMouseClicked(evt);
            }
        });

        SubPrinting.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        SubPrinting.setForeground(new java.awt.Color(255, 255, 255));
        SubPrinting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        SubPrinting.setText("   PRINT");
        SubPrinting.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SubPrinting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SubPrintingMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pendingReq, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4)
                                .addComponent(acc_user, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(SubPrinting, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(acc_user, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pendingReq, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SubPrinting, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("GYM MEMBERSHIP SUSBCRIPTION");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-arrow-back-24 (1).png"))); // NOI18N
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(0, 204, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        subTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(subTable);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 432, 590));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image.setText("No Image Inserted");
        jPanel4.add(image, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 180));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(438, 10, 350, 200));

        sid.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sid.setForeground(new java.awt.Color(204, 0, 0));
        sid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        sid.setEnabled(false);
        jPanel3.add(sid, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 270, 220, 30));

        sfname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sfname.setForeground(new java.awt.Color(204, 0, 0));
        sfname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel3.add(sfname, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 350, 220, 30));

        slname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        slname.setForeground(new java.awt.Color(204, 0, 0));
        slname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        jPanel3.add(slname, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 390, 220, 30));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("End Date         :");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 470, 110, 30));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Start  Date        :");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 430, 110, 30));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Lastname         :");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 390, 110, 30));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Firstname         :");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 350, 110, 30));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Users ID         :");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 110, 30));

        select.setText("SELECT");
        select.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        jPanel3.add(select, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 220, -1, 30));

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
        jPanel3.add(remove, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 220, -1, 30));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Sub Type         :");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 510, 110, 30));

        sstatus.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        sstatus.setForeground(new java.awt.Color(51, 51, 51));
        sstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Expired", "Pending" }));
        sstatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        sstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sstatusActionPerformed(evt);
            }
        });
        jPanel3.add(sstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 550, 220, 30));

        uid.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        uid.setForeground(new java.awt.Color(204, 0, 0));
        uid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        uid.setEnabled(false);
        uid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uidActionPerformed(evt);
            }
        });
        jPanel3.add(uid, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 310, 220, 30));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Subs ID          :");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 270, 110, 30));

        subtype.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        subtype.setForeground(new java.awt.Color(51, 51, 51));
        subtype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", "Yearly" }));
        subtype.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        subtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subtypeActionPerformed(evt);
            }
        });
        jPanel3.add(subtype, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 510, 220, 30));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Status         :");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 550, 110, 30));

        sdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        sdate.setEnabled(false);
        jPanel3.add(sdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 430, 220, 30));

        edate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        edate.setEnabled(false);
        jPanel3.add(edate, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 470, 220, 30));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                    .addComponent(time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(86, 86, 86)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        ManagerDash ud = new ManagerDash();
        ud.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
              JFileChooser fileChooser = new JFileChooser();
int returnValue = fileChooser.showOpenDialog(null);
if (returnValue == JFileChooser.APPROVE_OPTION) {
    try {
        selectedFile = fileChooser.getSelectedFile();
        destination = "src/images/" + selectedFile.getName();
        path = selectedFile.getAbsolutePath();

        if (FileExistenceChecker(path) == 1) {
            JOptionPane.showMessageDialog(null, "File Already Exists, Rename or Choose Another!");
            destination = "";
            path = "";
        } else {
            ImageIcon resizedImage = ResizeImage(path, null, image);
            if (resizedImage != null) {
                image.setIcon(resizedImage);
                select.setEnabled(false);
                remove.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to resize image. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        System.out.println("File Error: " + ex.getMessage());
        JOptionPane.showMessageDialog(null, "An error occurred while selecting the file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
 
    }//GEN-LAST:event_selectActionPerformed

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

        rs = dbc.getData("SELECT * FROM tbl_sub WHERE s_id = '" + sId + "'");

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
                
                // Check if end date is before current date
                java.util.Date currentDate = new java.util.Date();
                if (sqlEDate.before(new java.sql.Date(currentDate.getTime()))) {
                    sstatus.setSelectedItem("Expired");
                } else {
                    sstatus.setSelectedItem(rs.getString("s_status"));
                }
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
            pst.setString(1, firstName);
            pst.setString(2, lastName);
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

    private void removeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeMouseClicked
 
    }//GEN-LAST:event_removeMouseClicked

    private void deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseClicked
        int rowIndex = subTable.getSelectedRow();
        if(rowIndex < 0){
            JOptionPane.showMessageDialog(null, "Please select data first from the table","Confirm",JOptionPane.INFORMATION_MESSAGE);
        }else{
            TableModel model = subTable.getModel();
            Object value = model.getValueAt(rowIndex, 0);
            String id = value.toString();
            int a = JOptionPane.showConfirmDialog(null, "Are you sure to delete ID: "+id);
            if(a == JOptionPane.YES_OPTION){
                DbConnector dbc = new DbConnector();
                int s_id = Integer.parseInt(id);
                dbc.deleteData(s_id, "tbl_sub", "s_id");
                displayData();
                
            }
        }
    }//GEN-LAST:event_deleteMouseClicked

    private void subtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subtypeActionPerformed
         updateEndDateBasedOnSubtype();
    }//GEN-LAST:event_subtypeActionPerformed

    private void uidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_uidActionPerformed

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

    private void pendingReqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingReqMouseClicked
       PendingRequest uf = new PendingRequest();
            uf.setVisible(true);
            this.dispose(); 
    }//GEN-LAST:event_pendingReqMouseClicked

    private void SubPrintingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SubPrintingMouseClicked
  int rowIndex = subTable.getSelectedRow();
if (rowIndex < 0) {
    JOptionPane.showMessageDialog(null, "Please select an Item", "Warning", JOptionPane.WARNING_MESSAGE);
} else {
    try {
        DbConnector dbc = new DbConnector();
        TableModel tbl = subTable.getModel();
        String sId = tbl.getValueAt(rowIndex, 0).toString();

        if (sId == null || sId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ResultSet rs = dbc.getData("SELECT * FROM tbl_sub WHERE s_id = '" + sId + "'");

        if (rs.next()) {
            SubPrinting up = new SubPrinting();
            up.sid.setText(rs.getString("s_id"));
            up.uid.setText(rs.getString("u_id"));
            up.sfname.setText(rs.getString("s_fname"));
            up.slname.setText(rs.getString("s_lname"));

            // Retrieve and set start date (s_sdate)
            java.sql.Date sqlSDate = rs.getDate("s_sdate");
            if (sqlSDate != null) {
                up.sdate.setDate(new java.util.Date(sqlSDate.getTime()));
            }

            // Retrieve and set end date (s_edate)
            java.sql.Date sqlEDate = rs.getDate("s_edate");
            if (sqlEDate != null) {
                up.edate.setDate(new java.util.Date(sqlEDate.getTime()));

                // Check if end date is before current date
                java.util.Date currentDate = new java.util.Date();
                if (sqlEDate.before(new java.sql.Date(currentDate.getTime()))) {
                    up.status.setText("Expired");  // Setting text for status label
                } else {
                    up.status.setText(rs.getString("s_status"));  // Setting text for status label
                }
            }

            String imagePath = rs.getString("s_image");
            System.out.println("Debug: Image path from DB = " + imagePath);

            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon resizedImage = ResizeImage(imagePath, null, up.image);
                if (resizedImage != null) {
                    up.image.setIcon(resizedImage);
                } else {
                    System.out.println("Debug: Resized image is null");
                    up.image.setIcon(null);  // Ensure previous image is cleared if resizing fails
                }
            } else {
                System.out.println("Debug: Image path is null or empty");
                up.image.setIcon(null);  // Clear image display if no image path is found
            }

            up.subType.setText(rs.getString("s_subtype"));  // Setting text for subtype label
            up.setVisible(true);
            this.dispose();

            rs.close();  // Close ResultSet
            dbc.connect.close();  // Close database connection
        } else {
            JOptionPane.showMessageDialog(null, "No data found for the selected item.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
    }
}

    }//GEN-LAST:event_SubPrintingMouseClicked

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
            java.util.logging.Logger.getLogger(SubscribersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SubscribersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SubscribersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SubscribersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SubscribersForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel SubPrinting;
    private javax.swing.JLabel acc_user;
    private javax.swing.JLabel add;
    private javax.swing.JLabel clear;
    private javax.swing.JLabel date;
    private javax.swing.JLabel delete;
    public com.toedter.calendar.JDateChooser edate;
    public javax.swing.JLabel image;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel pendingReq;
    public javax.swing.JButton remove;
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
