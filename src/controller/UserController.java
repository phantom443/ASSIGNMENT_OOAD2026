package controller;

import dao.UserDAO;
import model.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController
{
    UserDAO usrDAO = new UserDAO();

    public void addUser(User user) throws SQLException
    {
        usrDAO.addUser(user);
    }
    public List<User> selectAllUsers() throws SQLException
    {
        return usrDAO.selectAllUsers();
    }
    public boolean deleteUser(String userID) throws SQLException
    {
        return usrDAO.deleteUser(userID);
    }
     public void updateUser(User user) throws SQLException
     {
     usrDAO.updateUser(user);
     }
    public User getUserID(String userID) throws SQLException
    {
        return usrDAO.getUserID(userID);
    }

}
