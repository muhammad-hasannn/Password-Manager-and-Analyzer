package logindao;

import model.User;
import java.sql.*;

public class UserDAO {

    // 1. method to Add User
    public boolean addUser(User u){

        String sql = "INSERT INTO Users(Username, PasswordHash) Values(?, ?)";

        // making connection and preparing statement
        try(Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);){

            // filling placeholders (?)
            pstmt.setString(1, u.getUsername());
            pstmt.setString(2, u.getPasswordHash());

            pstmt.executeUpdate();
            return true;
        }catch(SQLException e){
            return false;
        }
    }

    // 2. method search user
    public boolean searchUser(String username){
        String sql = "SELECT 1 FROM Users WHERE Username = ?";

        try(Connection con = DBConnection.getConnection();
        PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error in finding user: " + e.getMessage());
        }

        return false;
    }

    // 3. method get user
    public User getUser(String username){
        User user = null;
        String sql = "SELECT * FROM Users WHERE Username = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                // getString() is a method of the ResultSet class that extracts data from a specific column in the current row.

                // user = new User(rs.getString(1), rs.getString(2)); // If table order changes, code will break

                user = new User(rs.getString("Username"), rs.getString("PasswordHash"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error in getting user: " + e.getMessage());
        }
        return user;
    }
}
