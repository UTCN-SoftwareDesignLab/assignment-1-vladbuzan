package model.validation;

import model.Client;
import model.DTO.ClientDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//TODO
public class ClientDTOValidator {

    private final List<String> errors;
    private final ClientDTO client;
    private Client parsedClient;
    private static final String PCN_REGEX = "[a-zA-Z0-9]{9}";
    public ClientDTOValidator(ClientDTO client) {
        this.client = client;
        errors = new ArrayList<>();
        parsedClient = new Client();
    }

    public boolean validate() {
        return validateAddress() && validateName() &&
                validatePNC() && validateIdentityCardNumber();
    }

    public Client getClient() {
        parsedClient.setName(client.getName());
        parsedClient.setIdentityCardNumber(Long.parseLong(client.getIdentityCardNumber()));
        parsedClient.setAddress(client.getAddress());
        parsedClient.setPersonalNumericalCode(client.getPersonalNumericalCode());
        return parsedClient;
    }

    private boolean validateName() {
        if(client.getName().isEmpty()) {
            errors.add("Invalid name");
            return false;
        }
        return true;
    }

    private boolean validateIdentityCardNumber() {
        try{
            Long l = Long.parseLong(client.getIdentityCardNumber());
            if (l < 99999 || l > 1000000) {
                errors.add("Identification number must be 6 digits long");
                return false;
            }
        } catch (Exception e) {
            errors.add("Identification number must be of type long");
            return false;
        }
        return true;
    }

    public boolean validateAddress() {
        if(client.getAddress().isEmpty()) {
            errors.add("Address is empty");
            return false;
        }
        return true;
    }

    public boolean validatePNC() {
        if (!Pattern.compile(PCN_REGEX).matcher(client.getPersonalNumericalCode()).matches()) {
            errors.add("Invalid personal numeric code");
            return false;
        }
        return true;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getFormattedErrors() {
        String s = new String();
        for(String e : errors) {
            s += e + "\n";
        }
        return s;
    }
}
