package service.client;

import model.Client;
import model.ClientAccount;
import model.validation.Notification;

import java.util.List;

public interface ClientService {
    Notification<Boolean> addClient(Client client);
    Notification<List<Client>> getAll();
    Notification<Client> getClientById(Long id);
    Notification<Boolean> updateClient(Long id, Client client);
    Notification<Boolean> updateClientAccount(Long id, ClientAccount account);
    Notification<Boolean> removeClient(Long id);
    Notification<Boolean> transfer(Long senderId, Long receiverId, double amount);
}
