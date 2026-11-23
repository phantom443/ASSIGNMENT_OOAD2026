package model;

public class BankTeller extends User {

    private String tellerID;

    public BankTeller(String tellerID, String name, String surname, String address, int contact, String password) {
        super(tellerID, name, surname, address, contact, password);
        this.tellerID = tellerID;
    }

    public BankTeller(String userID, String name, String surname, String address, int contact, String password, String tellerID) {
        super(userID, name, surname, address, contact, password);
        this.tellerID = tellerID;
    }

    public String getTellerID() {
        return tellerID;
    }

    public void setTellerID(String tellerID) {
        this.tellerID = tellerID;
    }

}
