package view;

import controller.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerGUI extends JFrame {
    private User customer;
    private AccountController accountController;
    private TransactionController transactionController;

    public CustomerGUI(User customer) {
        this.customer = customer;
        this.accountController = new AccountController();
        this.transactionController = new TransactionController();

        setTitle("Customer Portal - " + customer.getName() + " " + customer.getSurname());
        setSize(400, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Welcome, " + customer.getName() + " " + customer.getSurname(), JLabel.CENTER));
        panel.add(new JLabel("User ID: " + customer.getUserID()));
        panel.add(new JLabel("Address: " + customer.getAddress()));
        panel.add(new JLabel("Contact: " + customer.getContact()));

        JButton btnViewAccounts = new JButton("View My Accounts");
        panel.add(btnViewAccounts);

        JButton btnMakeTransaction = new JButton("Make Transaction");
        panel.add(btnMakeTransaction);

        JButton btnLogout = new JButton("Logout");
        panel.add(btnLogout);

        btnViewAccounts.addActionListener(e -> showAccountsAndTransactions());

        btnMakeTransaction.addActionListener(e -> openTransactionDialog());

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginGUI().setVisible(true);
        });

        add(panel);
    }

    private void showAccountsAndTransactions() {
        try {
            List<Account> allAccounts = accountController.searchAccount();

            DefaultTableModel accountModel = new DefaultTableModel(
                    new String[]{"Account ID", "Branch ID", "Current Balance"}, 0
            );

            Set<String> userAccountIds = new HashSet<>();
            for (Account a : allAccounts) {
                if (a.getUserID() != null && a.getUserID().equals(customer.getUserID())) {
                    accountModel.addRow(new Object[]{a.getAccountID(), a.getBranchID(), a.getBalance()});
                    userAccountIds.add(a.getAccountID());
                }
            }

            JTable accountTable = new JTable(accountModel);

            DefaultTableModel transactionModel = new DefaultTableModel(
                    new String[]{"Transaction ID", "Account ID", "Type", "Amount"}, 0
            );

            List<Transaction> allTransactions = transactionController.selectAllTransactions();
            for (Transaction t : allTransactions) {
                if (userAccountIds.contains(t.getAccountID())) {
                    transactionModel.addRow(new Object[]{
                            t.getTransactionID(),
                            t.getAccountID(),
                            t.getTransactionType(),
                            t.isAmount()
                    });
                }
            }

            JTable transactionTable = new JTable(transactionModel);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("My Accounts", new JScrollPane(accountTable));
            tabbedPane.addTab("Transaction History", new JScrollPane(transactionTable));

            JOptionPane.showMessageDialog(
                    this,
                    tabbedPane,
                    "Account & Transaction Details - " + customer.getUserID(),
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private String generateTransactionId() {
        return "TX-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void openTransactionDialog() {
        try {
            List<Account> allAccounts = accountController.searchAccount();
            JComboBox<String> cmbAccountId = new JComboBox<>();
            for (Account a : allAccounts) {
                if (a.getUserID() != null && a.getUserID().equals(customer.getUserID())) {
                    cmbAccountId.addItem(a.getAccountID());
                }
            }

            if (cmbAccountId.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "You do not have any accounts yet.", "No Accounts", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JComboBox<String> cmbTxType = new JComboBox<>(new String[]{"Deposit", "Withdrawal"});
            JTextField txtAmount = new JTextField();

            JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
            form.add(new JLabel("Account ID:"));
            form.add(cmbAccountId);
            form.add(new JLabel("Transaction Type:"));
            form.add(cmbTxType);
            form.add(new JLabel("Amount:"));
            form.add(txtAmount);

            int result = JOptionPane.showConfirmDialog(this, form, "New Transaction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String accId = (String) cmbAccountId.getSelectedItem();
            String txType = (String) cmbTxType.getSelectedItem();
            float amount = Float.parseFloat(txtAmount.getText().trim());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a positive amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double delta = "Deposit".equalsIgnoreCase(txType) ? amount : -amount;
            boolean updated = accountController.updateBalance(accId, delta);
            if (!updated) {
                JOptionPane.showMessageDialog(this, "Insufficient funds for withdrawal.", "Transaction Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String txId = generateTransactionId();
            transactionController.addTransaction(new Transaction(txId, accId, txType, amount));

            Account updatedAccount = accountController.getAccountID(accId);
            JOptionPane.showMessageDialog(
                    this,
                    "Transaction " + txId + " successful! Current Balance: " + (updatedAccount != null ? updatedAccount.getBalance() : ""),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

}