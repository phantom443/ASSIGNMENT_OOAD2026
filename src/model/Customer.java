package model;

public class Customer extends User {
    public Customer() {
    }

    public Customer(String userID, String name, String surname, String address, int contact, String password) {
        super(userID, name, surname, address, contact, password);
    }

    @Override
    public String toString() {
        return "Customer{}";
    }
}
