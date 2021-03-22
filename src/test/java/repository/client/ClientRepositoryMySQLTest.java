package repository.client;

import database.Bootstrapper;
import database.DBConnectionFactory;
import model.Client;
import model.ClientAccount;
import model.generators.UniqueLongGenerator;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class ClientRepositoryMySQLTest  {

    public static Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
    public static ClientRepository repository = new ClientRepositoryMySQL(connection);

    @BeforeClass
    public static void setup() throws SQLException {
        Bootstrapper b = new Bootstrapper(connection);
        b.execute(true);
    }

    @Test
    public void addClient() throws SQLException {
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(1231235L);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        repository.addClient(client);
    }

    @Test
    public void getClientByICN() throws SQLException {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        repository.addClient(client);
        Client fetched = repository.getClientByICN(icn);
        Assert.assertEquals("Vlad Buzan", fetched.getName());

    }

    @Test
    public void updateClient() throws SQLException {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        repository.addClient(client);
        client = repository.getClientByICN(client.getIdentityCardNumber());
        client.setName("Nume nou");
        repository.updateClient(client.getId(), client);
        Client fetched = repository.getClientById(client.getId());
        Assert.assertEquals("Nume nou", fetched.getName());
    }

    @Test
    public void removeClient() throws SQLException {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        repository.addClient(client);
        client = repository.getClientByICN(client.getIdentityCardNumber());
        repository.removeClient(client.getId());
        Assert.assertNull(repository.getClientById(client.getId()));
    }

    @Test
    public void getAll() throws SQLException {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        repository.addClient(client);
        Assert.assertTrue(repository.getAll().size() > 0);
    }

    @Test
    public void updateClientAccount() throws SQLException {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        ClientAccount account = new ClientAccount();
        account.setIdentificationNumber(UniqueLongGenerator.generateUniqueLong());
        account.setType("regular");
        account.setAmount(12321.3213);
        account.setDateOfCreation(DateTime.now());
        client.setAccount(account);
        repository.addClient(client);
        client = repository.getClientByICN(client.getIdentityCardNumber());
        account.setType("new");
        repository.updateClientAccount(client.getId(), account);
        client = repository.getClientByICN(client.getIdentityCardNumber());
        Assert.assertEquals("new", client.getAccount().getType());

    }
}