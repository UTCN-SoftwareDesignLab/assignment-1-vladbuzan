package controller;

import model.Client;
import model.validation.Notification;
import service.client.ClientService;
import service.user.UserSession;
import view.TransferView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TransferController implements Observer, Observable{

    private final ClientService clientService;
    private final UserSession userSession;
    private final TransferView transferView;

    private final List<Observer> observers;

    public TransferController(ClientService clientService, UserSession userSession,
                              TransferView transferView) {
        this.clientService = clientService;
        this.userSession = userSession;
        this.transferView = transferView;

        observers = new ArrayList<>();

        transferView.setTransferBtnListener(new TransferBtnListener());
        transferView.setBackBtnListener(new BackBtnListener());
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
        if(getClass().equals(toAlert) &&
                (userSession.getLoggedInUser().getRole().getRole().equals("user"))) {
            transferView.setSenderList(clientService.getAll().getResult().toArray(new Client[0]));
            transferView.setReceiverList(clientService.getAll().getResult().toArray(new Client[0]));
            transferView.setVisible(true);
        }
    }

    private class TransferBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Client sender =transferView.getSelectedSender();
            Client receiver = transferView.getSelectedReceiver();
            Double amount;
            try {
                amount = Double.parseDouble(transferView.getAmount());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(transferView.getContentPane(),
                        "Please provide a double for the amount");
                return;
            }
            Notification<Boolean> notification = clientService.transfer(sender.getId(), receiver.getId(),
                    amount);
            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(transferView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(transferView.getContentPane(), "Transfer performed successfully");
            }
            transferView.setSenderList(clientService.getAll().getResult().toArray(new Client[0]));
            transferView.setReceiverList(clientService.getAll().getResult().toArray(new Client[0]));
        }

    }

    private class BackBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            transferView.setVisible(false);
            alertObservers(UserController.class);
        }
    }
}
