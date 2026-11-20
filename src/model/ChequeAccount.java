package model;

public class ChequeAccount extends Account
{
    private String employerName;
    private String employerAddress;

    public ChequeAccount(String accountID, String customerID, String branchID, float balance) {
        super(accountID, customerID, branchID, balance);
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }

    @Override
    public void calculateInterest(){}

    @Override
    public void payInterest(){}

}
