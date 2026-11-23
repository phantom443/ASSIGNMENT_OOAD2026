package view;

import controller.*;
import dao.BankTellerDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BankingGUI extends JFrame {

    private static final String CARD_MENU = "MENU";
    private static final String CARD_USERS = "USERS";
    private static final String CARD_ACCOUNTS = "ACCOUNTS";
    private static final String CARD_BRANCHES = "BRANCHES";
    private static final String CARD_TRANSACTIONS = "TRANSACTIONS";

    private UserController userController;
    private AccountController accountController;
    private BranchController branchController;
    private TransactionController transactionController;
    private BankTellerDAO bankTellerDAO;

    private CardLayout cardLayout;
    private JPanel mainContainer;

    private JTextField txtUserId, txtName, txtSurname, txtAddress, txtContact;
    private JPasswordField txtPassword;
    private JButton btnSubmitUserForm;
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private String currentUserMode = "ADD";

    private JTextField txtAccountId, txtAccUserId, txtBranchId, txtBalance;
    private JTextField txtEmployerName, txtEmployerAddress, txtInitialDeposit;
    private JTextField txtCompanyName, txtTaxNumber, txtRegNumber;
    private JButton btnSubmitAccountForm;
    private JTable accountTable;
    private DefaultTableModel accountTableModel;
    private String currentAccountMode = "ADD";

    private JTextField txtBrId, txtBrName, txtBrLocation;
    private JTextField txtTxAccId;
    private JComboBox<String> cmbTxType;
    private JTextField txtTxAmount;

    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;

    public BankingGUI() {
        userController = new UserController();
        accountController = new AccountController();
        branchController = new BranchController();
        transactionController = new TransactionController();
        bankTellerDAO = new BankTellerDAO();

        setTitle("Banking Management System");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new LoginGUI().setVisible(true);
            }
        });

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createMenuPanel(), CARD_MENU);
        mainContainer.add(wrapWithBackHeader("User Management", createUserPanel()), CARD_USERS);
        mainContainer.add(wrapWithBackHeader("Account Management", createAccountPanel()), CARD_ACCOUNTS);
        mainContainer.add(wrapWithBackHeader("Branch Management", createBranchPanel()), CARD_BRANCHES);
        mainContainer.add(wrapWithBackHeader("Transaction Management", createTransactionPanel()), CARD_TRANSACTIONS);

        add(mainContainer);
        showScreen(CARD_MENU);
    }

    private void showScreen(String cardName) {
        cardLayout.show(mainContainer, cardName);
    }

    private void openUserScreen(String mode) {
        currentUserMode = mode;
        switch (mode) {
            case "UPDATE" -> btnSubmitUserForm.setText("Save User Changes");
            case "DELETE" -> btnSubmitUserForm.setText("Confirm Delete User");
            default -> btnSubmitUserForm.setText("Save New User");
        }
        showScreen(CARD_USERS);
    }

    private void openAccountScreen(String mode) {
        currentAccountMode = mode;
        if ("DELETE".equals(mode)) {
            btnSubmitAccountForm.setText("Confirm Delete Account");
        } else {
            btnSubmitAccountForm.setText("Save New Account");
        }
        showScreen(CARD_ACCOUNTS);
    }

    private JPanel wrapWithBackHeader(String title, JComponent content) {
        JPanel screen = new JPanel(new BorderLayout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JButton btnBack = new JButton("← Back to Menu");
        btnBack.addActionListener(e -> showScreen(CARD_MENU));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));

        topBar.add(btnBack, BorderLayout.WEST);
        topBar.add(lblTitle, BorderLayout.CENTER);
        topBar.add(Box.createHorizontalStrut(btnBack.getPreferredSize().width), BorderLayout.EAST);

        screen.add(topBar, BorderLayout.NORTH);
        screen.add(content, BorderLayout.CENTER);
        return screen;
    }

    private JPanel createMenuPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel heading = new JLabel("Banking Operations", SwingConstants.CENTER);
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 20f));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        outer.add(heading, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        JPanel grid = new JPanel(new GridLayout(2, 2, 18, 18));
        grid.setPreferredSize(new Dimension(760, 360));

        JButton btnAddUser = new JButton("Add User");
        JButton btnUpdateUser = new JButton("Update User");
        JButton btnDeleteUser = new JButton("Delete User");
        JButton btnSearchUser = new JButton("Search User");
        JButton btnDisplayUsers = new JButton("Display All Users");

        btnAddUser.addActionListener(e -> openUserScreen("ADD"));
        btnUpdateUser.addActionListener(e -> openUserScreen("UPDATE"));
        btnDeleteUser.addActionListener(e -> openUserScreen("DELETE"));
        btnSearchUser.addActionListener(e -> {
            showScreen(CARD_USERS);
            searchUserById();
        });
        btnDisplayUsers.addActionListener(e -> {
            showScreen(CARD_USERS);
            loadAllUsers();
        });
        JPanel usersCard = createModuleCard("Users", btnAddUser, btnUpdateUser, btnDeleteUser, btnSearchUser, btnDisplayUsers);

        JButton btnAddAccount = new JButton("Add Account");
        JButton btnDeleteAccount = new JButton("Delete Account");
        JButton btnSearchAccount = new JButton("Search Account");
        JButton btnDisplayAccounts = new JButton("Display All Accounts");

        btnAddAccount.addActionListener(e -> openAccountScreen("ADD"));
        btnDeleteAccount.addActionListener(e -> openAccountScreen("DELETE"));
        btnSearchAccount.addActionListener(e -> {
            showScreen(CARD_ACCOUNTS);
            searchAccountById();
        });
        btnDisplayAccounts.addActionListener(e -> {
            showScreen(CARD_ACCOUNTS);
            loadAllAccounts();
        });
        JPanel accountsCard = createModuleCard("Accounts", btnAddAccount, btnDeleteAccount, btnSearchAccount, btnDisplayAccounts);

        JButton btnAddBranch = new JButton("Add Branch");
        btnAddBranch.addActionListener(e -> showScreen(CARD_BRANCHES));
        JPanel branchesCard = createModuleCard("Branches", btnAddBranch);

        JButton btnNewTransaction = new JButton("New Transaction");
        JButton btnSearchTxByAcc = new JButton("Search Tx by Account");
        JButton btnAllTransactions = new JButton("All Transactions");

        btnNewTransaction.addActionListener(e -> showScreen(CARD_TRANSACTIONS));
        btnSearchTxByAcc.addActionListener(e -> {
            showScreen(CARD_TRANSACTIONS);
            searchTransactionsByAccountId();
        });
        btnAllTransactions.addActionListener(e -> {
            showScreen(CARD_TRANSACTIONS);
            loadTransactions();
        });
        JPanel transactionsCard = createModuleCard("Transactions", btnNewTransaction, btnSearchTxByAcc, btnAllTransactions);

        grid.add(usersCard);
        grid.add(accountsCard);
        grid.add(branchesCard);
        grid.add(transactionsCard);

        centerWrapper.add(grid);
        outer.add(centerWrapper, BorderLayout.CENTER);
        return outer;
    }

    private JPanel createModuleCard(String title, JButton... actionButtons) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        card.setFocusable(false);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 15f));
        lblTitle.setFocusable(false);
        card.add(lblTitle, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonsPanel.setFocusable(false);
        for (JButton btn : actionButtons) {
            btn.setFocusPainted(false);
            buttonsPanel.add(btn);
        }

        card.add(buttonsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));

        formPanel.add(new JLabel("User ID:"));
        txtUserId = new JTextField();
        formPanel.add(txtUserId);

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Surname:"));
        txtSurname = new JTextField();
        formPanel.add(txtSurname);

        formPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        formPanel.add(txtAddress);

        formPanel.add(new JLabel("Contact:"));
        txtContact = new JTextField();
        formPanel.add(txtContact);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnSubmitUserForm = new JButton("Save New User");
        JButton btnSearchUser = new JButton("Search by ID");
        JButton btnDisplayAllUsers = new JButton("Display All Users");
        JButton btnRefreshUser = new JButton("Refresh");

        btnPanel.add(btnSubmitUserForm);
        btnPanel.add(btnSearchUser);
        btnPanel.add(btnDisplayAllUsers);
        btnPanel.add(btnRefreshUser);

        formPanel.add(new JLabel(""));
        formPanel.add(btnPanel);

        formPanel.add(new JLabel("Bank Teller:"));
        JPanel tellerBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnAddTeller = new JButton("Add Teller");
        btnAddTeller.addActionListener(e -> addBankTeller());
        tellerBtnPanel.add(btnAddTeller);
        formPanel.add(tellerBtnPanel);

        panel.add(formPanel, BorderLayout.NORTH);

        userTableModel = new DefaultTableModel(
                new String[]{"User ID", "Name", "Surname", "Address", "Contact", "Role"}, 0
        );
        userTable = new JTable(userTableModel);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        btnSubmitUserForm.addActionListener(e -> {
            try {
                String userId = txtUserId.getText().trim();
                if ("DELETE".equals(currentUserMode)) {
                    boolean success = userController.deleteUser(userId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "User deleted successfully!");
                        loadAllUsers();
                        clearUserFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete user.");
                    }
                    return;
                }

                if ("UPDATE".equals(currentUserMode)) {
                    User user = new User(
                            userId,
                            txtName.getText().trim(),
                            txtSurname.getText().trim(),
                            txtAddress.getText().trim(),
                            Integer.parseInt(txtContact.getText().replaceAll("[^0-9]", "")),
                            new String(txtPassword.getPassword())
                    );
                    userController.updateUser(user);
                    JOptionPane.showMessageDialog(this, "User updated successfully!");
                    loadAllUsers();
                    clearUserFields();
                    return;
                }

                addUser();
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnSearchUser.addActionListener(e -> searchUserById());
        btnDisplayAllUsers.addActionListener(e -> loadAllUsers());
        btnRefreshUser.addActionListener(e -> clearUserFields());

        return panel;
    }

    private void addUser() {
        try {
            String userId = txtUserId.getText().trim();
            String name = txtName.getText().trim();
            String surname = txtSurname.getText().trim();
            String address = txtAddress.getText().trim();
            String contactDigits = txtContact.getText().replaceAll("[^0-9]", "");
            String password = new String(txtPassword.getPassword());

            if (userId.isEmpty() || name.isEmpty() || surname.isEmpty() || contactDigits.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in User ID, Name, Surname, and Contact.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int contact = Integer.parseInt(contactDigits);
            User user = new User(userId, name, surname, address, contact, password);

            userController.addUser(user);
            loadAllUsers();
            clearUserFields();

            JOptionPane.showMessageDialog(this, "User added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric contact number.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void addBankTeller() {
        try {
            String tellerId = txtUserId.getText().trim();
            String name = txtName.getText().trim();
            String surname = txtSurname.getText().trim();
            String address = txtAddress.getText().trim();
            String contactDigits = txtContact.getText().replaceAll("[^0-9]", "");
            String password = new String(txtPassword.getPassword());

            if (tellerId.isEmpty() || name.isEmpty() || surname.isEmpty() || contactDigits.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in Teller ID, Name, Surname, and Contact.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int contact = Integer.parseInt(contactDigits);
            BankTeller teller = new BankTeller(tellerId, name, surname, address, contact, password);

            bankTellerDAO.addTeller(teller);

            userTableModel.addRow(new Object[]{
                    teller.getTellerID(),
                    teller.getName(),
                    teller.getSurname(),
                    teller.getAddress(),
                    teller.getContact(),
                    "Bank Teller"
            });

            JOptionPane.showMessageDialog(this, "Bank Teller added and displayed in User tab!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid contact number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchUserById() {
        String userId = txtUserId.getText().trim();
        if (userId.isEmpty()) {
            userId = JOptionPane.showInputDialog(this, "Enter User ID to search:");
            if (userId == null) return;
            userId = userId.trim();
            txtUserId.setText(userId);
        }

        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID.");
            return;
        }

        try {
            User user = userController.getUserID(userId);
            userTableModel.setRowCount(0);
            if (user != null) {
                txtName.setText(user.getName());
                txtSurname.setText(user.getSurname());
                txtAddress.setText(user.getAddress());
                txtContact.setText(String.valueOf(user.getContact()));
                userTableModel.addRow(new Object[]{
                        user.getUserID(),
                        user.getName(),
                        user.getSurname(),
                        user.getAddress(),
                        user.getContact(),
                        "User"
                });
            } else {
                JOptionPane.showMessageDialog(this, "No user found with ID: " + userId);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadAllUsers() {
        try {
            userTableModel.setRowCount(0);
            List<User> users = userController.selectAllUsers();
            for (User user : users) {
                userTableModel.addRow(new Object[]{
                        user.getUserID(),
                        user.getName(),
                        user.getSurname(),
                        user.getAddress(),
                        user.getContact()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void clearUserFields() {
        txtUserId.setText("");
        txtName.setText("");
        txtSurname.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtPassword.setText("");
        userTable.clearSelection();
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        JPanel formPanel = new JPanel(new GridLayout(11, 2, 3, 3));
        formPanel.setBorder(BorderFactory.createTitledBorder("Account Details"));

        formPanel.add(new JLabel("Account ID:"));
        txtAccountId = new JTextField();
        formPanel.add(txtAccountId);

        formPanel.add(new JLabel("User ID:"));
        txtAccUserId = new JTextField();
        formPanel.add(txtAccUserId);

        formPanel.add(new JLabel("Branch ID:"));
        txtBranchId = new JTextField();
        formPanel.add(txtBranchId);

        formPanel.add(new JLabel("Balance:"));
        txtBalance = new JTextField();
        formPanel.add(txtBalance);

        formPanel.add(new JLabel("Employer Name :"));
        txtEmployerName = new JTextField();
        formPanel.add(txtEmployerName);

        formPanel.add(new JLabel("Employer Address:"));
        txtEmployerAddress = new JTextField();
        formPanel.add(txtEmployerAddress);

        formPanel.add(new JLabel("Initial Deposit :"));
        txtInitialDeposit = new JTextField();
        formPanel.add(txtInitialDeposit);

        formPanel.add(new JLabel("Company Name:"));
        txtCompanyName = new JTextField();
        formPanel.add(txtCompanyName);

        formPanel.add(new JLabel("Tax Number:"));
        txtTaxNumber = new JTextField();
        formPanel.add(txtTaxNumber);

        formPanel.add(new JLabel("Registration Number:"));
        txtRegNumber = new JTextField();
        formPanel.add(txtRegNumber);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnSubmitAccountForm = new JButton("Save New Account");
        JButton btnSearchAccount = new JButton("Search by ID");
        JButton btnDisplayAllAccounts = new JButton("Display All Accounts");
        JButton btnRefreshAccount = new JButton("Refresh");

        btnPanel.add(btnSubmitAccountForm);
        btnPanel.add(btnSearchAccount);
        btnPanel.add(btnDisplayAllAccounts);
        btnPanel.add(btnRefreshAccount);

        formPanel.add(new JLabel(""));
        formPanel.add(btnPanel);

        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setPreferredSize(new Dimension(0, 260));
        panel.add(formScroll, BorderLayout.NORTH);

        accountTableModel = new DefaultTableModel(new String[]{"Account ID", "User ID", "Branch ID", "Balance"}, 0);
        accountTable = new JTable(accountTableModel);
        panel.add(new JScrollPane(accountTable), BorderLayout.CENTER);

        btnSubmitAccountForm.addActionListener(e -> {
            try {
                String accId = txtAccountId.getText().trim();
                if ("DELETE".equals(currentAccountMode)) {
                    boolean success = accountController.deleteAccount(accId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Account deleted successfully!");
                        loadAllAccounts();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete account.");
                    }
                    return;
                }

                String userId = txtAccUserId.getText().trim();
                String branchId = txtBranchId.getText().trim();
                float balance = Float.parseFloat(txtBalance.getText().trim());

                Account account;

                if (!txtInitialDeposit.getText().trim().isEmpty()) {
                    int initialDeposit = Integer.parseInt(txtInitialDeposit.getText().trim());
                    account = new InvestmentAccount(accId, userId, branchId, balance, initialDeposit);
                } else if (!txtEmployerName.getText().trim().isEmpty()) {
                    ChequeAccount chqAcc = new ChequeAccount(accId, userId, branchId, balance);
                    chqAcc.setEmployerName(txtEmployerName.getText().trim());
                    chqAcc.setEmployerAddress(txtEmployerAddress.getText().trim());
                    account = chqAcc;
                } else {
                    account = new Account(accId, userId, branchId, balance);
                }

                accountController.addAccount(account);
                JOptionPane.showMessageDialog(this, "Account added successfully!");
                loadAllAccounts();
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnSearchAccount.addActionListener(e -> searchAccountById());
        btnDisplayAllAccounts.addActionListener(e -> loadAllAccounts());
        btnRefreshAccount.addActionListener(e -> clearAccountFields());

        return panel;
    }

    private void searchAccountById() {
        String accId = txtAccountId.getText().trim();
        if (accId.isEmpty()) {
            accId = JOptionPane.showInputDialog(this, "Enter Account ID to search:");
            if (accId == null) return;
            accId = accId.trim();
            txtAccountId.setText(accId);
        }

        if (accId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Account ID.");
            return;
        }

        try {
            Account account = accountController.getAccountID(accId);
            accountTableModel.setRowCount(0);
            if (account != null) {
                txtAccUserId.setText(account.getUserID());
                txtBranchId.setText(account.getBranchID());
                txtBalance.setText(String.valueOf(account.getBalance()));
                accountTableModel.addRow(new Object[]{
                        account.getAccountID(),
                        account.getUserID(),
                        account.getBranchID(),
                        account.getBalance()
                });
            } else {
                JOptionPane.showMessageDialog(this, "No account found with ID: " + accId);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadAllAccounts() {
        try {
            accountTableModel.setRowCount(0);
            List<Account> accounts = accountController.searchAccount();
            for (Account account : accounts) {
                accountTableModel.addRow(new Object[]{
                        account.getAccountID(),
                        account.getUserID(),
                        account.getBranchID(),
                        account.getBalance()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void clearAccountFields() {
        txtAccountId.setText("");
        txtAccUserId.setText("");
        txtBranchId.setText("");
        txtBalance.setText("");
        txtEmployerName.setText("");
        txtEmployerAddress.setText("");
        txtInitialDeposit.setText("");
        txtCompanyName.setText("");
        txtTaxNumber.setText("");
        txtRegNumber.setText("");
        accountTable.clearSelection();
    }

    private JPanel createBranchPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Branch Details"));

        panel.add(new JLabel("Branch ID:"));
        txtBrId = new JTextField();
        panel.add(txtBrId);

        panel.add(new JLabel("Branch Name:"));
        txtBrName = new JTextField();
        panel.add(txtBrName);

        panel.add(new JLabel("Location:"));
        txtBrLocation = new JTextField();
        panel.add(txtBrLocation);

        panel.add(new JLabel(""));
        JButton btnAddBranch = new JButton("Add Branch");
        panel.add(btnAddBranch);

        btnAddBranch.addActionListener(e -> {
            try {
                Branch branch = new Branch();
                branch.setBranchID(txtBrId.getText().trim());
                branch.setBranchName(txtBrName.getText().trim());
                branch.setLocation(txtBrLocation.getText().trim());

                branchController.addBranch(branch);
                JOptionPane.showMessageDialog(this, "Branch added successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JPanel container = new JPanel(new BorderLayout());
        container.add(panel, BorderLayout.NORTH);
        return container;
    }

    private String generateTransactionId() {
        return "TX-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Transaction Details"));

        panel.add(new JLabel("Account ID:"));
        txtTxAccId = new JTextField();
        panel.add(txtTxAccId);

        panel.add(new JLabel("Transaction Type:"));
        cmbTxType = new JComboBox<>(new String[]{"Deposit", "Withdrawal"});
        panel.add(cmbTxType);

        panel.add(new JLabel("Amount:"));
        txtTxAmount = new JTextField();
        panel.add(txtTxAmount);

        panel.add(new JLabel(""));
        JButton btnAddTransaction = new JButton("Add Transaction");
        panel.add(btnAddTransaction);

        JButton btnSearchTxByAccId = new JButton("Search by Account ID");
        JButton btnDisplayAllTransactions = new JButton("Display All Transactions");
        panel.add(btnSearchTxByAccId);
        panel.add(btnDisplayAllTransactions);

        btnAddTransaction.addActionListener(e -> {
            try {
                String accId = txtTxAccId.getText().trim();
                String txType = (String) cmbTxType.getSelectedItem();
                float amount = Float.parseFloat(txtTxAmount.getText().trim());

                if (accId.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter Account ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Account account = accountController.getAccountID(accId);
                if (account == null) {
                    JOptionPane.showMessageDialog(this, "Account not found: " + accId, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double delta = "Deposit".equalsIgnoreCase(txType) ? amount : -amount;
                boolean updated = accountController.updateBalance(accId, delta);
                if (!updated) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds or unable to update balance.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String txId = generateTransactionId();
                Transaction transaction = new Transaction(txId, accId, txType, amount);
                transactionController.addTransaction(transaction);

                Account refreshedAccount = accountController.getAccountID(accId);
                float newBalance = refreshedAccount != null ? refreshedAccount.getBalance() : account.getBalance();

                JOptionPane.showMessageDialog(this, "Transaction " + txId + " recorded! New balance: " + newBalance);
                txtTxAmount.setText("");
                loadTransactions();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        btnSearchTxByAccId.addActionListener(e -> searchTransactionsByAccountId());
        btnDisplayAllTransactions.addActionListener(e -> loadTransactions());

        transactionTableModel = new DefaultTableModel(
                new String[]{"Transaction ID", "Account ID", "Type", "Amount"}, 0
        );
        transactionTable = new JTable(transactionTableModel);

        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.add(panel, BorderLayout.NORTH);
        container.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        return container;
    }

    private void searchTransactionsByAccountId() {
        String accId = txtTxAccId.getText().trim();
        if (accId.isEmpty()) {
            accId = JOptionPane.showInputDialog(this, "Enter Account ID to search transactions:");
            if (accId == null) return;
            accId = accId.trim();
            txtTxAccId.setText(accId);
        }

        if (accId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Account ID.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            transactionTableModel.setRowCount(0);
            // If your TransactionController has getTransactionsByAccountId(accId), you can call that directly instead.
            List<Transaction> transactions = transactionController.selectAllTransactions();
            int matchCount = 0;
            for (Transaction tx : transactions) {
                if (accId.equalsIgnoreCase(tx.getAccountID())) {
                    transactionTableModel.addRow(new Object[]{
                            tx.getTransactionID(),
                            tx.getAccountID(),
                            tx.getTransactionType(),
                            tx.isAmount()
                    });
                    matchCount++;
                }
            }
            if (matchCount == 0) {
                JOptionPane.showMessageDialog(this, "No transactions found for Account ID: " + accId);
            }
        } catch (SQLException | NoSuchMethodError e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to search transactions. Check TransactionController / Transaction getters.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private void loadTransactions() {
        try {
            transactionTableModel.setRowCount(0);
            List<Transaction> transactions = transactionController.selectAllTransactions();
            for (Transaction tx : transactions) {
                transactionTableModel.addRow(new Object[]{
                        tx.getTransactionID(),
                        tx.getAccountID(),
                        tx.getTransactionType(),
                        tx.isAmount()
                });
            }
        } catch (SQLException | NoSuchMethodError e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load transactions. Check TransactionController / Transaction getters.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            e.printStackTrace();
        }
    }

}