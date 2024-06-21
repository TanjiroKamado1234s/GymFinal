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
public class PendingRequest extends javax.swing.JFrame {

   
    /**
     * Creates new form SubscribersForm
     */private Session sn;
    public PendingRequest() {
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
        String query = "SELECT s_id, u_id, s_fname, s_status FROM tbl_sub WHERE s_status = 'Pending'";
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
        delete1 = new javax.swing.JLabel();
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
        add.setEnabled(false);
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
        update.setEnabled(false);
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
        clear.setEnabled(false);
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
        delete.setEnabled(false);
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
        });

        acc_user.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        acc_user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        delete1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        delete1.setForeground(new java.awt.Color(255, 255, 255));
        delete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICONS/icons8-add-user-30.png"))); // NOI18N
        delete1.setText("   PENDING");
        delete1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        delete1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                delete1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(add, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(update, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(delete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(acc_user, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("GYM MEMBERSHIP SUBSCRIPTION");

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
        sfname.setEnabled(false);
        jPanel3.add(sfname, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 350, 220, 30));

        slname.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        slname.setForeground(new java.awt.Color(204, 0, 0));
        slname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        slname.setEnabled(false);
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
        select.setEnabled(false);
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        jPanel3.add(select, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 220, -1, 30));

        remove.setText("REMOVE");
        remove.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        remove.setEnabled(false);
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
        sstatus.setForeground(new java.awt.Color(204, 0, 0));
        sstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Active" }));
        sstatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        sstatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sstatusMouseClicked(evt);
            }
        });
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
        subtype.setForeground(new java.awt.Color(204, 0, 0));
        subtype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", "Yearly" }));
        subtype.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0), 2));
        subtype.setEnabled(false);
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
                .addGap(16, 16, 16)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        SubscribersForm ud = new SubscribersForm();
        ud.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
           
    }//GEN-LAST:event_selectActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
          
    }//GEN-LAST:event_removeActionPerformed

    private void sstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sstatusActionPerformed
        if (sid.getText().isEmpty()) {
    JOptionPane.showMessageDialog(null, "Please Select a Subscriber First");
} else {
    if (sfname.getText().isEmpty() || slname.getText().isEmpty() || sdate.getDate() == null || edate.getDate() == null) {
        JOptionPane.showMessageDialog(null, "All fields are required!");
    } else {
        String status = sstatus.getSelectedItem().toString().trim();
        if (!status.equalsIgnoreCase("active")) {
            JOptionPane.showMessageDialog(null, "Please changed the status to active to activate the Subscriber");
            return;
        }

        System.out.println("Debug: All fields are filled and status is active, proceeding with update");

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

            JOptionPane.showMessageDialog(null, "Subscriber Activated Successfully!");
            displayData();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }
    }
}

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

        String query = "SELECT * FROM tbl_sub WHERE s_id = ? AND s_status = 'pending'";
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
  
    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
                
    }//GEN-LAST:event_addMouseClicked

    private void updateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateMouseClicked

     
    }//GEN-LAST:event_updateMouseClicked

    private void clearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearMouseClicked
        
    }//GEN-LAST:event_clearMouseClicked

    private void removeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeMouseClicked
 
    }//GEN-LAST:event_removeMouseClicked

    private void deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseClicked
       
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

    private void delete1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delete1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_delete1MouseClicked

    private void sstatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sstatusMouseClicked
          

    }//GEN-LAST:event_sstatusMouseClicked

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
            java.util.logging.Logger.getLogger(PendingRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PendingRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PendingRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PendingRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PendingRequest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel acc_user;
    private javax.swing.JLabel add;
    private javax.swing.JLabel clear;
    private javax.swing.JLabel date;
    private javax.swing.JLabel delete;
    private javax.swing.JLabel delete1;
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
