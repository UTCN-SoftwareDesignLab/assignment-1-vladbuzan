package model.validation;

import model.Client;

public class TransferValidator {

    public static boolean validate(Client sender, Client receiver, double amount) {
        if(amount < 0) return false;
        return !(sender.getAccount().getAmount() < amount);
    }
}
