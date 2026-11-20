package model;

public class InvestmentAccount extends Account
{

    public InvestmentAccount(String accountID, String customerID, String branchID, float balance, int initialBalance) {
        super(accountID, customerID, branchID, balance);
        this.initialBalance = initialBalance;
    }

    private int initialBalance;

    public int getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }

    public void withdraw(){}
    public void depositFunds(){}

    @Override
    public void calculateInterest(){}

    @Override
    public void payInterest(){}
}
