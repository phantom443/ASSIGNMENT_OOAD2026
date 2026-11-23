package dao;
import model.*;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public void addAccount(Account account) throws SQLException
    {
        String sql = "INSERT INTO account (accountID, userID, branchID, balance, employerName, employerAddress, initialDeposit, companyName, taxNumber, registrationNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection()) {
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, account.getAccountID());
            statement.setString(2, account.getUserID());
            statement.setString(3, account.getBranchID());
            statement.setFloat(4, account.getBalance());

            if (account instanceof ChequeAccount) {
                ChequeAccount cheque = (ChequeAccount) account;
                statement.setString(5, cheque.getEmployerName());
                statement.setString(6, cheque.getEmployerAddress());
                statement.setNull(7, Types.INTEGER);

            } else if (account instanceof InvestmentAccount) {
                InvestmentAccount investment = (InvestmentAccount) account;
                statement.setNull(5, Types.VARCHAR);
                statement.setNull(6, Types.VARCHAR);
                statement.setInt(7, investment.getInitialBalance());

            } else {
                statement.setNull(5, Types.VARCHAR);
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.INTEGER);
            }
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserID(account.getUserID());
            if (user instanceof Company) {
                Company company = (Company) user;
                statement.setString(8, company.getCompanyName());
                statement.setString(9, company.getTaxNumber());
                statement.setString(10, company.getRegistrationNumber());
            } else {
                statement.setNull(8, Types.VARCHAR);
                statement.setNull(9, Types.VARCHAR);
                statement.setNull(10, Types.VARCHAR);
            }

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Account (" + account.getClass().getSimpleName() + ") successfully added!");
            }

        } catch (SQLException e) {
            System.err.println("Error while adding account: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Account> searchAccount() throws SQLException
    {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM account";
        try(Connection conn = DatabaseConnection.getConnection())
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            {
                while (rs.next()) {
                    Account account = new Account(rs.getString("accountID"), rs.getString("userID"), rs.getString("branchID"), rs.getFloat("balance"));
                    list.add(account);
                }
            }
        }
        return list;
    }

    public Account getAccountID(String accountID) throws SQLException
    {
        String sql = "SELECT * FROM account WHERE accountID = ?";
        Account account = null;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, accountID);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next())
            {
               account = new Account();
                account.setAccountID(rs.getString("accountID"));
                account.setUserID(rs.getString("userID"));
                account.setBranchID(rs.getString("branchID"));
                account.setBalance(rs.getFloat("balance"));
            }
        }
        return account;
    }

    public boolean deleteAccount(String accountID)
    {
        String sql = "DELETE FROM account WHERE accountID = ?";

        try(Connection conn= DatabaseConnection.getConnection())
        {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBalance(String accountID, double delta) throws SQLException {
        String sql = "UPDATE account SET balance = balance + ? WHERE accountID = ? AND balance + ? >= 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, delta);
            pstmt.setString(2, accountID);
            pstmt.setDouble(3, delta);
            return pstmt.executeUpdate() > 0;
        }
    }
    public void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE account SET branchID = ? WHERE accountID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getBranchID());
            pstmt.setString(2, account.getAccountID());
            pstmt.executeUpdate();
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        String accountId = rs.getString("accountID");
        String userId = rs.getString("userID");
        String branchId = rs.getString("branchID");
        float balance = rs.getFloat("balance");

        int initialDeposit = rs.getInt("initialDeposit");
        boolean hasInitialDeposit = !rs.wasNull() && initialDeposit > 0;

        String employerName = rs.getString("employerName");
        String employerAddress = rs.getString("employerAddress");
        boolean hasEmployerInfo = (employerName != null && !employerName.isBlank())
                || (employerAddress != null && !employerAddress.isBlank());

        if (hasInitialDeposit) {
            return new InvestmentAccount(accountId, userId, branchId, balance, initialDeposit);
        }
        if (hasEmployerInfo) {
            ChequeAccount cheque = new ChequeAccount(accountId, userId, branchId, balance);
            cheque.setEmployerName(employerName);
            cheque.setEmployerAddress(employerAddress);
            return cheque;
        }
        return new Account(accountId, userId, branchId, balance);
    }

}

