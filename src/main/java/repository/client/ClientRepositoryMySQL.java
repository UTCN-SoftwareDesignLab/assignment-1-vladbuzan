package repository.client;

import model.Client;
import model.ClientAccount;
import org.joda.time.DateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepositoryMySQL implements ClientRepository{

    private final Connection connection;

    public ClientRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addClient(Client client) throws SQLException {
        PreparedStatement insertClient = connection.prepareStatement(
                "INSERT INTO client (name, identity_card_number, " +
                        "personal_numerical_code, address) " +
                        "VALUES (?, ?, ?, ?);"
        );
        insertClient.setString(1, client.getName());
        insertClient.setLong(2, client.getIdentityCardNumber());
        insertClient.setString(3, client.getPersonalNumericalCode());
        insertClient.setString(4, client.getAddress());
        insertClient.executeUpdate();
        if(client.getAccount() != null) {
            Client client1 = getClientByICN(client.getIdentityCardNumber());
            addClientAccount(client.getAccount(), client1.getId());
        }
    }

    @Override
    public List<Client> getAll() throws SQLException {
        Statement statement = connection.createStatement();
        String fetchSQL = "SELECT * FROM client;";
        ResultSet rs = statement.executeQuery(fetchSQL);
        List<Client> clients = new ArrayList<>();
        while(rs.next()) {
            Client client = getClientFromResultSet(rs);
            client.setAccount(getAccount(client.getId()));
            clients.add(client);
        }
        return clients;
    }

    @Override
    public Client getClientByICN(Long icn) throws SQLException {
        Statement statement = connection.createStatement();
        String fetchSQL = "SELECT * FROM client " +
                "WHERE identity_card_number = " + icn.toString() + ";";

        ResultSet rs = statement.executeQuery(fetchSQL);
        if(rs.next()) {
            Client client = getClientFromResultSet(rs);
            client.setAccount(getAccount(client.getId()));
            return client;
        }
        return null;
    }

    @Override
    public Client getClientById(Long id) throws SQLException {
        Statement statement = connection.createStatement();
        String fetchSQL = "SELECT * FROM client " +
                "WHERE id = " + id.toString() + ";";

        ResultSet rs = statement.executeQuery(fetchSQL);
        if(rs.next()) {
            Client client = getClientFromResultSet(rs);
            client.setAccount(getAccount(client.getId()));
            return client;
        }
        return null;
    }

    @Override
    public void updateClient(Long id, Client newClient) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE client " +
                        "SET name = ? , " +
                        "identity_card_number = ? , " +
                        "personal_numerical_code = ? , " +
                        "address = ?  " +
                        "WHERE id = " + id.toString() + " ;");
        preparedStatement.setString(1, newClient.getName());
        preparedStatement.setLong(2, newClient.getIdentityCardNumber());
        preparedStatement.setString(3, newClient.getPersonalNumericalCode());
        preparedStatement.setString(4, newClient.getAddress());
        preparedStatement.executeUpdate();
    }

    @Override
    public void removeClient(Long id) throws SQLException {
        Statement statement = connection.createStatement();
        String removeSQL = "DELETE FROM client " +
                "WHERE id = " + id.toString() + "; ";
        statement.executeUpdate(removeSQL);
    }

    @Override
    public void updateClientAccount(Long id, ClientAccount account) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE client_account " +
                        "SET type = ? ," +
                        "identification_number = ? ," +
                        "amount = ?, " +
                        "date_of_creation = ? " +
                        "WHERE client_id = " + id.toString() + " ;"
        );
        preparedStatement.setString(1, account.getType());
        preparedStatement.setLong(2, account.getIdentificationNumber());
        preparedStatement.setDouble(3 ,account.getAmount());
        preparedStatement.setDate(4, new Date(account.getDateOfCreation().getMillis()));
        preparedStatement.executeUpdate();
    }


    private ClientAccount getAccount(Long id) throws SQLException {
        Statement statement = connection.createStatement();
        String fetchSQL = "SELECT * FROM client_account " +
                "WHERE client_id = " + id.toString() + ";";

        ResultSet rs = statement.executeQuery(fetchSQL);
        if(rs.next()) {
            return getClientAccountFromResultSet(rs);
        }
        return null;
    }

    private void addClientAccount(ClientAccount account, Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO client_account (client_id, type, " +
                        "identification_number, amount, date_of_creation) " +
                        "VALUES (?, ?, ?, ?, ?); ");
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, account.getType());
        preparedStatement.setLong(3, account.getIdentificationNumber());
        preparedStatement.setDouble(4, account.getAmount());
        preparedStatement.setDate(5, new Date(account.getDateOfCreation().getMillis()));
        preparedStatement.executeUpdate();
    }

    private Client getClientFromResultSet (ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setAddress(rs.getString("address"));
        client.setPersonalNumericalCode(rs.getString("personal_numerical_code"));
        client.setName(rs.getString("name"));
        client.setIdentityCardNumber(rs.getLong("identity_card_number"));
        return client;
    }

    private ClientAccount getClientAccountFromResultSet (ResultSet rs) throws SQLException {
        ClientAccount account = new ClientAccount();
        account.setId(rs.getLong("id"));
        account.setAmount(rs.getDouble("amount"));
        account.setType(rs.getString("type"));
        account.setDateOfCreation(new DateTime(rs.getDate("date_of_creation").getTime()));
        account.setIdentificationNumber(rs.getLong("identification_number"));
        return account;
    }


}
