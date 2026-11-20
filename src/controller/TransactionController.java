package controller;

import dao.TransactionDAO;
import model.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionController
{
    TransactionDAO trnDAO = new TransactionDAO();

    public void addTransaction(Transaction transaction) throws SQLException
    {
        trnDAO.addTransaction(transaction);
    }

    public Transaction getTransactionByID(String transactionID) throws SQLException
    {
        return trnDAO.getTransactionById(transactionID);
    }

    public List<Transaction> selectAllTransactions() throws SQLException {
        return trnDAO.selectAllTransactions();
    }
}
