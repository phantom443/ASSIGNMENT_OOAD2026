package dao;
import model.*;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO
{
    public void addBranch(Branch branch) {
        String sql = "INSERT INTO branches (branchID, branchName, location) VALUES (?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection())
        {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, branch.getBranchID());
            statement.setString(2, branch.getBranchName());
            statement.setString(3, branch.getLocation());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0)
            {
                System.out.println("Branch successfully added to the database!");
            }
        } catch (SQLException e)
        {
            System.err.println("Error while adding branch: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Branch getBranchById(String branchID)
    {
        String sql = "SELECT * FROM branches WHERE branch_id = ?";
        Branch branch = null;

        try(Connection conn = DatabaseConnection.getConnection())
        {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, branchID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                String id = resultSet.getString("branch_id");
                String name = resultSet.getString("branch_name");
                String location = resultSet.getString("location");

            }
        } catch (SQLException e)
        {
            System.err.println("Error while retrieving branch: " + e.getMessage());
            e.printStackTrace();
        }

        return branch;
    }
}
