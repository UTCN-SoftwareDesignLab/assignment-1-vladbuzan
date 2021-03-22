package service.client;

import database.Constants;
import model.Action;
import model.Client;
import model.ClientAccount;
import model.validation.Notification;
import model.validation.TransferValidator;
import org.joda.time.DateTime;
import repository.action.ActionRepository;
import repository.client.ClientRepository;
import service.user.UserSession;

import java.sql.SQLException;
import java.util.List;

import static service.RightValidator.hasRight;
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;
    private final ActionRepository actionRepository;
    private final UserSession userSession;

    private static final String NO_RIGHT = "No privilege to perform this action";
    private static final String SQL_ERR = "Error while querying the database";

    public ClientServiceImpl(ClientRepository clientRepository, ActionRepository actionRepository, UserSession userSession) {
        this.clientRepository = clientRepository;
        this.actionRepository = actionRepository;
        this.userSession = userSession;
    }


    @Override
    public Notification<Boolean> addClient(Client client) {
        Notification<Boolean> notification = new Notification<>();

        if(client == null) {
            notification.addError("No client provided");
            return notification;
        }

        if(!hasRight(userSession.getLoggedInUser(), Constants.Rights.CREATE_CLIENT)) {
            notification.addError(NO_RIGHT);
            return notification;
        }

        try {
            clientRepository.addClient(client);
            Action action = new Action();
            action.setDescription(
                    "User " + userSession.getLoggedInUser().getUsername() + " " +
                            "added client " + client.getName() + " into the database.");
            action.setTime(DateTime.now());
            actionRepository.save(action, userSession.getLoggedInUser());
        } catch (SQLException throwables) {
            notification.addError(SQL_ERR);
            return notification;
        }
        notification.setResult(true);
        return notification;
    }

    @Override
    public Notification<List<Client>> getAll() {
        Notification<List<Client>> notification = new Notification<>();
        try {
            List<Client> clients = clientRepository.getAll();
            notification.setResult(clients);
        } catch (SQLException throwables) {
            notification.addError(SQL_ERR);
            return notification;
        }
        return notification;
    }

    @Override
    public Notification<Client> getClientById(Long id) {
        Notification<Client> notification = new Notification<>();

        if(id == null) {
            notification.addError("No ID provided");
            return notification;
        }

        try {
            notification.setResult(clientRepository.getClientById(id));
        } catch (SQLException throwables) {
            notification.addError(SQL_ERR);
            return notification;
        }
        return notification;
    }

    @Override
    public Notification<Boolean> updateClient(Long id, Client client) {
        Notification<Boolean> notification = new Notification<>();

        if(id == null) {
            notification.addError("No ID provided");
            return notification;
        }
        if(client == null) {
            notification.addError("No client provided");
            return notification;
        }

        try {
            clientRepository.updateClient(id, client);
            Action action = new Action();
            action.setDescription("User + " + userSession.getLoggedInUser().getUsername() +
                    " updated client info for " + client.getName());
            action.setTime(DateTime.now());
            actionRepository.save(action, userSession.getLoggedInUser());
        } catch (SQLException throwables) {
            notification.addError(SQL_ERR);
            return notification;
        }
        notification.setResult(true);
        return notification;
    }

    @Override
    public Notification<Boolean> updateClientAccount(Long id, ClientAccount account) {
        Notification<Boolean> notification = new Notification<>();

        if(id == null) {
            notification.addError("No ID provided");
            return notification;
        }
        if(account == null) {
            notification.addError("No account provided");
            return notification;
        }

        try {
            clientRepository.updateClientAccount(id, account);
            Action action = new Action();
            action.setDescription("User + " + userSession.getLoggedInUser().getUsername() +
                    " updated client info for " + account.getIdentificationNumber().toString());
            action.setTime(DateTime.now());
            actionRepository.save(action, userSession.getLoggedInUser());
        } catch (SQLException throwables) {
            notification.addError(SQL_ERR);
            return notification;
        }
        notification.setResult(true);
        return notification;

    }

    @Override
    public Notification<Boolean> removeClient(Long id) {
        Notification<Boolean> notification = new Notification<>();

        if(id == null) {
            notification.addError("No ID provided");
            return notification;
        }

        try {
            clientRepository.removeClient(id);
            Action action = new Action();
            action.setDescription("User " + userSession.getLoggedInUser().getUsername() +
                    " removed a client from the database");
            action.setTime(DateTime.now());
            actionRepository.save(action, userSession.getLoggedInUser());
        } catch (SQLException throwables) {
            notification.addError(SQL_ERR);
            return notification;
        }
        notification.setResult(true);
        return notification;
    }

    @Override
    public Notification<Boolean> transfer(Long senderId, Long receiverId, double amount) {
        Notification<Boolean> notification = new Notification<>();
        Client sender, receiver;
        try {
            sender = clientRepository.getClientById(senderId);
            receiver = clientRepository.getClientById(receiverId);
        } catch (SQLException throwables) {
            notification.addError(SQL_ERR);
            return notification;
        }
        if (sender == null) {
            notification.addError("No matching id for sender");
            return notification;
        }
        if (receiver == null) {
            notification.addError("No matching id for receiver");
            return notification;
        }
        if(!TransferValidator.validate(sender, receiver, amount)) {
            notification.addError("Invalid transfer");
            return notification;
        }

        ClientAccount senderAccount = sender.getAccount();
        ClientAccount receiverAccount = receiver.getAccount();

        senderAccount.setAmount(
                senderAccount.getAmount() - amount
        );
        receiverAccount.setAmount(
                receiverAccount.getAmount() + amount
        );

        try {
            clientRepository.updateClientAccount(senderId, senderAccount);
            clientRepository.updateClientAccount(receiverId, receiverAccount);
            Action action = new Action();
            action.setDescription("User " + userSession.getLoggedInUser().getUsername() + " " +
                    "performed a tranfser of " + amount + " between " + sender.getName() +" and " +
                    receiver.getName());
            action.setTime(DateTime.now());
            actionRepository.save(action, userSession.getLoggedInUser());
        } catch (SQLException throwables) {
            notification.addError(SQL_ERR);
            return notification;
        }

        notification.setResult(true);
        return notification;
    }
}
