package database;

import model.User;
import java.sql.*;


public class UserDAO {

    // 1. method to Add User
    public void addUser(User u){

        String sql = "INSERT INTO Users(Username, PasswordHash) Values(?, ?)";

        // making connection and preparing statement
        try(Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);){

            pstmt.setString(1, u.getUsername());
            pstmt.setString(2, u.getPasswordHash());

            pstmt.executeUpdate();
            System.out.println("User added successfully!");
        }catch(SQLException e){
            System.out.println("Error in adding user: " + e.getMessage());
        }
    }

    // 2. method search user
    public boolean searchUser(String username){


    }
}
