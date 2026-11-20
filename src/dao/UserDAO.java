package dao;
import model.*;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO
{
    public void addUser(User user) throws SQLException
    {
        String sql = "INSERT INTO User(userID,name,surname,address,contact,password) VALUES(?,?,?,?,?,?)";
        try(Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getSurname());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, user.getContact());
            pstmt.setString(6,user.getPassword());
            pstmt.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException
    {
        String sql = "UPDATE User SET userID = ?, name = ?, surname = ?, address = ?, contact = ?, password =? WHERE userID = ?";
        try(Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getSurname());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, user.getContact());
            pstmt.setString(6,user.getPassword());

            pstmt.setString(7, user.getUserID());
            pstmt.executeUpdate();
        }
    }

    public boolean deleteUser(String userId)
    {
        String sql = "DELETE FROM User WHERE userId = ?";

        try(Connection conn= DatabaseConnection.getConnection())
        {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> selectAllUsers() throws SQLException
    {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try(Connection conn = DatabaseConnection.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            {
                while(rs.next()) {
                    User user = new User(rs.getString("userID"), rs.getString("name"), rs.getString("surname"), rs.getString("address"), rs.getInt("contact"), rs.getString("password"));
                    list.add(user);
                }
            }
        }
        return list;
    }

    public User getUserID(String userID) throws SQLException
    {
        String sql = "SELECT * FROM user WHERE userID = ?";
        User user = null;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                user = new User(
                rs.getString("userID"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getInt("contact"),
                        rs.getString("password")
                );
            }
        }
        return user;
    }

    public User authenticate(String userID, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE userID = ? AND password = ?";
        User user = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userID);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("userID"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getInt("contact"),
                        rs.getString("password")
                );
            }
        }
        return user;
    }

}
