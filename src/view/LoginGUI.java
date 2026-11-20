package view;
import dao.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class LoginGUI extends JFrame
{
    private JTextField txtId;
    private JPasswordField txtPassword;
    private UserDAO userDAO;
    private BankTellerDAO bankTellerDAO;

    public LoginGUI() {
        userDAO = new UserDAO();
        bankTellerDAO = new BankTellerDAO();
        setTitle("Banking System - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Enter ID:"));
        txtId = new JTextField();
        panel.add(txtId);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        JButton btnClear = new JButton("Clear");
        panel.add(btnLogin);
        panel.add(btnClear);

        add(panel);

        btnLogin.addActionListener(e -> {
            String id = txtId.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (id.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both ID and Password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BankTeller teller = bankTellerDAO.authenticate(id, password);
                if (teller != null) {
                    JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + teller.getName());
                    dispose();
                    new BankingGUI().setVisible(true);
                    return;
                }

                User user = userDAO.authenticate(id, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Customer Login Successful! Welcome " + user.getName());
                    dispose();
                    new CustomerGUI(user).setVisible(true);
                    return;
                }

                JOptionPane.showMessageDialog(this, "Invalid ID or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        btnClear.addActionListener(e -> {
            txtId.setText("");
            txtPassword.setText("");
        });
    }
}
