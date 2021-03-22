package model;

import org.joda.time.DateTime;

public class ClientAccount {

    private Long id;
    private String type;
    private Long identificationNumber;
    private double amount;
    private DateTime dateOfCreation;

    public ClientAccount() {

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public DateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(DateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    @Override
    public String toString() {
        return id.toString() + " " + type +
                " " + identificationNumber.toString() + " " +
                Double.toString(amount) + " " + dateOfCreation.toString();
    }
}
