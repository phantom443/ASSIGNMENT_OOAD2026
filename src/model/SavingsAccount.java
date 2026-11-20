package model;

public class SavingsAccount extends Account
{
    public SavingsAccount(String accountID, String customerID, String branchID, float balance) {
        super(accountID, customerID, branchID, balance);
    }

    public void depositFunds(){}

    @Override
    public void calculateInterest(){}

    @Override
    public void payInterest(){}

    @Override
    public String toString() {
        return "SavingsAccount{}";
    }
}
