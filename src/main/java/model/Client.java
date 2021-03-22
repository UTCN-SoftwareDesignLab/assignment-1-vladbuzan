package model;


public class Client {

    private Long id;
    private String name;
    private Long identityCardNumber;
    private String personalNumericalCode;
    private String address;
    private ClientAccount account;

    public Client () {

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(Long identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getPersonalNumericalCode() {
        return personalNumericalCode;
    }

    public void setPersonalNumericalCode(String personalNumericalCode) {
        this.personalNumericalCode = personalNumericalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ClientAccount getAccount() {
        return account;
    }

    public void setAccount(ClientAccount account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return id.toString() + " " + name +
                " " + identityCardNumber.toString() + " " +
                " " + personalNumericalCode + " " +
                address + " " + '\n' + account.toString();
    }
}
