package model;

public class Account implements InterestBearing {
    private String accountID;
    private String userID;
    private String branchID;
    private float balance;
    private float interestRate;

    public Account() {
    }

    public Account(String accountID, String userID, String branchID, float balance) {
        this.accountID = accountID;
        this.userID = userID;
        this.branchID = branchID;
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public float calculateInterest() {
        return balance * (interestRate / 12.0f);
    }

    @Override
    public void payInterest() {
        float monthlyInterest = calculateInterest();
        if (monthlyInterest > 0) {
            this.balance += monthlyInterest;
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountID='" + accountID + '\'' +
                ", customerID='" + userID + '\'' +
                ", branchID='" + branchID + '\'' +
                ", balance=" + balance +
                ", interestRate=" + interestRate +
                '}';
    }
}