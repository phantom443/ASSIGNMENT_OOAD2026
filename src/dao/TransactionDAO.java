package dao;
import model.*;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO
{
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction (transactionID, accountID, transactionType, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, transaction.getTransactionID());
            statement.setString(2, transaction.getAccountID());
            statement.setString(3, transaction.getTransactionType());
            statement.setFloat(4, transaction.isAmount());
            statement.executeUpdate();
        }
    }

    public Transaction getTransactionById(String transactionID) {
        String sql = "SELECT * FROM transaction WHERE transactionID = ?";
        Transaction transaction = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, transactionID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String txId = resultSet.getString("transactionID");
                String accId = resultSet.getString("accountID");
                String txType = resultSet.getString("transactionType");
                float amount = resultSet.getFloat("amount");

                transaction = new Transaction(txId, accId, txType, amount);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving transaction: " + e.getMessage());
            e.printStackTrace();
        }

        return transaction;
    }

    public List<Transaction> selectAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getString("transactionID"),
                        rs.getString("accountID"),
                        rs.getString("transactionType"),
                        rs.getFloat("amount")
                );
                list.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving all transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
