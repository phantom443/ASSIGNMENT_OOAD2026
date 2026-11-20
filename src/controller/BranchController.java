package controller;
import dao.BranchDAO;
import model.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class BranchController
{
    BranchDAO brnDAO = new BranchDAO();
    public void addBranch(Branch branch) throws SQLException
    {
        brnDAO.addBranch(branch);
    }

    public Branch getBranchByID(String branchID) throws SQLException
    {
        return brnDAO.getBranchById(branchID);
    }
}
