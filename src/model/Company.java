package model;

public class Company extends Customer
{
    private String companyName;
    private String taxNumber;
    private String registrationNumber;

    public Company(String userID, String name, String surname, String address, int contact, String password, String companyName, String taxNumber, String registrationNumber) {
        super(userID, name, surname, address, contact, password);
        this.companyName = companyName;
        this.taxNumber = taxNumber;
        this.registrationNumber = registrationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyName='" + companyName + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }
}
