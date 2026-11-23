package model;

public class InvestmentAccount extends Account {
    private static final float DEFAULT_INVESTMENT_RATE = 0.05f;
    private int initialBalance;

    public InvestmentAccount(String accountID, String customerID, String branchID, float balance, int initialBalance) {
        super(accountID, customerID, branchID, balance);
        this.initialBalance = initialBalance;
        setInterestRate(DEFAULT_INVESTMENT_RATE);
    }

    public int getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }

    public void withdraw(float amount) {
        if (amount > 0 && getBalance() >= amount) {
            setBalance(getBalance() - amount);
        }
    }

    public void depositFunds(float amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
        }
    }

    @Override
    public float calculateInterest() {
        // Example rule: only earn interest if balance stays at or above initialBalance
        if (getBalance() >= initialBalance) {
            return super.calculateInterest();
        }
        return 0.0f;
    }
}