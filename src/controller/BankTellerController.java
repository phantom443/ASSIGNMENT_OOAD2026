package controller;

import dao.BankTellerDAO;
import model.*;
import java.sql.SQLException;

public class BankTellerController
{
    BankTellerDAO bktDAO = new BankTellerDAO();

    public void addTeller(BankTeller bankTeller) throws SQLException
    {
        bktDAO.addTeller(bankTeller);
    }
}
