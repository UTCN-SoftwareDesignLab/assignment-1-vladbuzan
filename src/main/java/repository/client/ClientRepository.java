package repository.client;

import model.Client;
import model.ClientAccount;

import java.sql.SQLException;
import java.util.List;

public interface ClientRepository {

    void addClient(Client client) throws SQLException;
    List<Client> getAll() throws SQLException;
    Client getClientByICN(Long icn) throws SQLException;
    Client getClientById(Long id) throws SQLException;
    void updateClient(Long id, Client newClient) throws SQLException;
    void removeClient(Long id) throws SQLException;
    void updateClientAccount(Long id, ClientAccount account) throws SQLException;
}
