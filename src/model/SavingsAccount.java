package model;

public class SavingsAccount extends Account {
    private static final float DEFAULT_SAVINGS_RATE = 0.0005f;

    public SavingsAccount(String accountID, String customerID, String branchID, float balance) {
        super(accountID, customerID, branchID, balance);
        setInterestRate(DEFAULT_SAVINGS_RATE);
    }

    public void depositFunds(float amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
        }
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "accountID='" + getAccountID() + '\'' +
                ", balance=" + getBalance() +
                ", monthlyInterest=" + calculateInterest() +
                '}';
    }
}