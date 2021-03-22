package controller;

import model.Client;
import model.DTO.ClientAccountDTO;
import model.DTO.ClientDTO;
import model.validation.ClientAccountDTOValidator;
import model.validation.ClientDTOValidator;
import model.validation.Notification;
import org.joda.time.DateTime;
import service.client.ClientService;
import service.user.UserSession;
import view.UserView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserController implements Observable, Observer{

    private final ClientService clientService;
    private final UserSession userSession;
    private final UserView userView;

    private final List<Observer> observers;

    public UserController(ClientService clientService, UserSession userSession
            , UserView userView) {
        this.clientService = clientService;
        this.userSession = userSession;
        this.userView = userView;
        observers = new ArrayList<>();

        userView.setAddClientBtnListener(new AddClientBtnListener());
        userView.setRemoveClientBtnListener(new RemoveClientBtnListener());
        userView.setUpdateClientBtnListener(new UpdateClientBtnListener());
        userView.setUpdateClientAccountBtnListener(new UpdateClientAccountBtnListener());
        userView.setTransferBtnListener(new TransferBtnListener());
        userView.setBackBtnListener(new BackBtnListener());
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void alertObservers(Class toAlert) {
        observers.forEach(o -> o.alert(toAlert));
    }

    @Override
    public void alert(Class toAlert) {
        if(getClass().equals(toAlert)) {
            if(userSession.getLoggedInUser().getRole().getRole().equals("user")) {
                userView.setVisible(true);
                userView.setClientList(clientService.getAll().getResult().toArray(Client[]::new));
            }
        }
    }

    private class AddClientBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ClientDTO client = new ClientDTO();
            client.setName(userView.getName());
            client.setIdentityCardNumber(userView.getIdcn());
            client.setPersonalNumericalCode(userView.getPnc());
            client.setAddress(userView.getAddress());

            ClientAccountDTO account = new ClientAccountDTO();
            account.setAmount(userView.getAmount());
            account.setType(userView.getAccountType());
            account.setIdentificationNumber(userView.getIdentif());

            ClientDTOValidator clientValidator = new ClientDTOValidator(client);
            ClientAccountDTOValidator accountValidator = new ClientAccountDTOValidator(account);

            if(!clientValidator.validate()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), clientValidator.getFormattedErrors());
                return;
            }

            if(!accountValidator.validate()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), accountValidator.getFormattedErrors());
                return;
            }

            Client parsedClient = clientValidator.getClient();
            parsedClient.setAccount(accountValidator.getParsedAccount());
            parsedClient.getAccount().setDateOfCreation(DateTime.now());

            Notification<Boolean> notification = clientService.addClient(parsedClient);
            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(userView.getContentPane(), "Client added successfully");
                userView.setClientList(clientService.getAll().getResult().toArray(Client[]::new));
            }
        }
    }

    private class RemoveClientBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Notification<Boolean> notification = clientService.removeClient(
                    userView.getSelectedClient().getId()
            );
            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(userView.getContentPane(), "Client removed successfully");
                userView.setClientList(clientService.getAll().getResult().toArray(Client[]::new));
            }
        }
    }

    private class UpdateClientBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            ClientDTO client = new ClientDTO();
            client.setName(userView.getName());
            client.setIdentityCardNumber(userView.getIdcn());
            client.setPersonalNumericalCode(userView.getPnc());
            client.setAddress(userView.getAddress());

            ClientDTOValidator clientValidator = new ClientDTOValidator(client);
            if(!clientValidator.validate()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), clientValidator.getFormattedErrors());
                return;
            }

            Notification<Boolean> notification = clientService.updateClient(
                    userView.getSelectedClient().getId(), clientValidator.getClient()
            );

            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(userView.getContentPane(), "Client updated successfully");
                userView.setClientList(clientService.getAll().getResult().toArray(Client[]::new));
            }
        }
    }

    private class UpdateClientAccountBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ClientAccountDTO account = new ClientAccountDTO();
            account.setAmount(userView.getAmount());
            account.setType(userView.getAccountType());
            account.setIdentificationNumber(userView.getIdentif());

            ClientAccountDTOValidator accountValidator = new ClientAccountDTOValidator(account);

            if(!accountValidator.validate()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), accountValidator.getFormattedErrors());
                return;
            }
            accountValidator.getParsedAccount().setDateOfCreation(DateTime.now());
            Notification<Boolean> notification = clientService.updateClientAccount(
                    userView.getSelectedClient().getId(), accountValidator.getParsedAccount()
            );
            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(userView.getContentPane(), "Client account updated successfully");
                userView.setClientList(clientService.getAll().getResult().toArray(Client[]::new));
            }


        }
    }

    //TODO transfer
    private class TransferBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            userView.setVisible(false);
            alertObservers(TransferController.class);
        }
    }

    private class BackBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            userView.setVisible(false);
            userSession.endSession();
            alertObservers(LoginController.class);
        }
    }

}
