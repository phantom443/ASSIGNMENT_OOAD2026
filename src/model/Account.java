package model;

public class Account implements InterestBearing
{
    private String accountID;
    private String userID;
    private String branchID;
    private float balance;

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

    @Override
    public String toString() {
        return "Account{" +
                "accountID='" + accountID + '\'' +
                ", customerID='" + userID + '\'' +
                ", branchID='" + branchID + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public void calculateInterest() {

    }

    @Override
    public void payInterest() {

    }
}
