/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author AJ
 */
public class DbConnector {
    
      public Connection connect;
    
     public DbConnector(){
            try{
                connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp_sys", "root", "");
            }catch(SQLException ex){
                    System.out.println("Can't connect to database: "+ex.getMessage());
            }
        }
     
     public Connection connect_db(){
              try{
                connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp_sys", "root", "");
            }catch(SQLException ex){
                    System.out.println("Can't connect to database: "+ex.getMessage());
            }
              return connect;
        }
        

        public ResultSet getData(String sql) throws SQLException{
            Statement stmt = connect.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            return rst;
        }

        public boolean insertData(String sql){
            try{
                PreparedStatement pst = connect.prepareStatement(sql);
                pst.executeUpdate();
                System.out.println("Inserted Successfully!");
                pst.close();
               return true;
            }catch(SQLException ex){
                System.out.println("Connection Error: "+ex);
               return false;
            }
        }
        
        //Function to update data
        public void updateData(String sql){
            try{
                PreparedStatement pst = connect.prepareStatement(sql);
                    int rowsUpdated = pst.executeUpdate();
                        if(rowsUpdated > 0){
                            
                        }else{
                            System.out.println("Data Update Failed!");
                        }
                        pst.close();
            }catch(SQLException ex){
                System.out.println("Connection Error: "+ex);
            }
        
        }
        
        //Function to delete data
        public void deleteData(int id, String table, String table_id){
           try{
                PreparedStatement pst = connect.prepareStatement("DELETE FROM "+table+" WHERE "+table_id+" = ?");
                pst.setInt(1, id);
                int rowsDeleted = pst.executeUpdate();
                    if(rowsDeleted > 0){
                        JOptionPane.showMessageDialog(null, "Deleted Successfully!");
                    }else{
                        System.out.println("Deletion Failed!");
                    }
                    pst.close();
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null, "Data cannot be deleted\nContact the administrator.");
            }
        }
        
}



