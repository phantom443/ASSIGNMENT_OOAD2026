package dao;
import model.*;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankTellerDAO
{
    public void addTeller(BankTeller bankTeller) throws SQLException
    {
        String sql = "INSERT INTO bankTeller(tellerID,name,surname,address,contact,password) VALUES(?,?,?,?,?,?)";
        try(Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, bankTeller.getTellerID());
            pstmt.setString(2, bankTeller.getName());
            pstmt.setString(3, bankTeller.getSurname());
            pstmt.setString(4, bankTeller.getAddress());
            pstmt.setInt(5, bankTeller.getContact());
            pstmt.setString(6,bankTeller.getPassword());
            pstmt.executeUpdate();
        }
    }

    public BankTeller authenticate(String tellerID, String password) throws SQLException {
        String sql = "SELECT * FROM bankTeller WHERE tellerID = ? AND password = ?";
        BankTeller teller = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tellerID);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                teller = new BankTeller(
                        rs.getString("tellerID"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getInt("contact"),
                        rs.getString("password")
                );
            }
        }
        return teller;
    }
}
