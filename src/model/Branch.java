package model;

import java.util.List;

public class Branch
{
    private String branchName;
    private String branchID;
    private String location;
    private List<Account> accounts;

    public Branch() {
    }
    public Branch(String branchName, String branchID, String location, List<Account> accounts) {
        this.branchName = branchName;
        this.branchID = branchID;
        this.location = location;
        this.accounts = accounts;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    /**
    public Account getAccount(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    } **/
    @Override
    public String toString() {
        return "Branch{" +
                "branchName='" + branchName + '\'' +
                ", branchID='" + branchID + '\'' +
                ", location='" + location + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
