package model;

public class Transaction {
    private String transactionID;
    private String accountID;
    private String transactionType;
    private float amount;

    public Transaction() {
    }

    public Transaction(String transactionID, String accountID, String transactionType, float amount) {
        this.transactionID = transactionID;
        this.accountID = accountID;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public float isAmount() {
        return amount;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transcation{" +
                "transactionID='" + transactionID + '\'' +
                ", accountID='" + accountID + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                '}';
    }
}
