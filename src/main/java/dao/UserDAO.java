package dao;

import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public boolean registerUser(User user) {

        try(Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO users(username,email,password_hash) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean emailExists(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
   
    
    public User getUserByEmail(String email) {

        String sql = "SELECT user_id, email, username,password_hash FROM users WHERE email=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password_hash")); 
                user.setUsername(rs.getString("username"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
}