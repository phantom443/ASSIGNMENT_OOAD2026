package controller;
import model.*;
import dao.AccountDAO;
import java.sql.SQLException;
import java.util.List;

public class AccountController
{
    AccountDAO accDAO = new AccountDAO();

    public void addAccount(Account account) throws SQLException
    {
        accDAO.addAccount(account);
    }
    public  List<Account> searchAccount() throws SQLException
    {
        return accDAO.searchAccount();
    }
    public boolean deleteAccount(String accountID) throws SQLException
    {
        return accDAO.deleteAccount(accountID);
    }
    public boolean updateBalance(String accountID, double delta) throws SQLException {
        return accDAO.updateBalance(accountID, delta);
    }

    public void updateAccount(Account account) throws SQLException
    {
        accDAO.updateAccount(account);
    }

    public Account getAccountID(String accountID) throws SQLException
    {
        return accDAO.getAccountID(accountID);
    }

}
