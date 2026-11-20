package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection
{
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/assignmentooad?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD ="";
    private DatabaseConnection() {

    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Connection successful! " + conn);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
