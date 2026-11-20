package model;

public class User
{
    private String userID;
    private String name;
    private String surname;
    private String address;
    private int contact;
    private String password;

    public User() {
    }

    public User(String userID, String name, String surname, String address, int contact, String password) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.contact = contact;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
