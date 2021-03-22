package model.validation;

import database.Constants;
import model.ClientAccount;
import model.DTO.ClientAccountDTO;

import java.util.ArrayList;
import java.util.List;
//TODO
public class ClientAccountDTOValidator {
    private final List<String> errors;
    private final ClientAccountDTO account;
    private ClientAccount parsedAccount;

    public ClientAccountDTOValidator(ClientAccountDTO account) {
        this.account = account;
        errors = new ArrayList<>();
        parsedAccount = new ClientAccount();
    }

    public boolean validate() {
        return validateAmount() && validateType() && validateIdentificationNumber();
    }

    public ClientAccount getParsedAccount() {
        parsedAccount.setType(account.getType());
        parsedAccount.setIdentificationNumber(Long.parseLong(account.getIdentificationNumber()));
        parsedAccount.setAmount(Double.parseDouble(account.getAmount()));
        return parsedAccount;
    }

    private boolean validateType() {
        String type = account.getType();
        if(type.equals(Constants.ClientTypes.REGULAR)) {
            return true;
        }
        if(type.equals(Constants.ClientTypes.COMPANY)) {
            return true;
        }
        if(type.equals(Constants.ClientTypes.PREMIUM)) {
            return true;
        }
        errors.add("Invalid type");
        return false;
    }

    private boolean validateIdentificationNumber() {
        try{
            Long l = Long.parseLong(account.getIdentificationNumber());
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

    private boolean validateAmount() {
        try{
            Double l = Double.parseDouble(account.getAmount());
            if (l < 0) {
                errors.add("Amount number must be positive");
                return false;
            }
        } catch (Exception e) {
            errors.add("Amount number must be of type double");
            return false;
        }
        return true;
    }
    public String getFormattedErrors() {
        String s = new String();
        for(String e : errors) {
            s += e + "\n";
        }
        return s;
    }
}
