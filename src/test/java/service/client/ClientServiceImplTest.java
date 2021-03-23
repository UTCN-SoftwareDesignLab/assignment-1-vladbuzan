package service.client;

import database.Bootstrapper;
import database.DBConnectionFactory;
import launcher.ComponentFactory;
import model.Client;
import model.ClientAccount;
import model.generators.UniqueLongGenerator;
import model.validation.Notification;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClientServiceImplTest  {

    private static ClientService clientService;
    private static ComponentFactory factory = ComponentFactory.instance(true);
    @BeforeClass
    public static void setup() throws SQLException {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        Bootstrapper b = new Bootstrapper(connection);
        b.execute(true);
        clientService = factory.getClientService();
        factory.getAuthenticationService().login("user", "user");
    }

    @Test
    public void addClient() {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        Notification<Boolean> notification = clientService.addClient(client);
        Assert.assertFalse(notification.hasErrors());
    }

    @Test
    public void getAll() {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        Notification<Boolean> notification = clientService.addClient(client);
        Notification<List<Client>> notification1 = clientService.getAll();
        Assert.assertFalse(notification1.hasErrors());
        Assert.assertTrue(notification1.getResult().size()>0);

    }

    @Test
    public void getById() throws SQLException {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        Notification<Boolean> notification = clientService.addClient(client);
        Client client1 = factory.getClientRepository().getClientByICN(icn);
        Notification<Client> notification1 = clientService.getClientById(client1.getId());
        Assert.assertFalse(notification1.hasErrors());
        Assert.assertEquals("Vlad Buzan", notification1.getResult().getName());
    }

    @Test
    public void updateClient() throws SQLException {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        clientService.addClient(client);
        client = factory.getClientRepository().getClientByICN(icn);
        client.setName("nou");
        Notification<Boolean> notification = clientService.updateClient(client.getId(), client);
        Assert.assertFalse(notification.hasErrors());
    }

    @Test
    public void updateClientAccount() throws SQLException{
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        ClientAccount account = new ClientAccount();
        account.setAmount(200.0);
        account.setType("regular");
        account.setDateOfCreation(DateTime.now());
        account.setIdentificationNumber(123123L);
        client.setAccount(account);
        clientService.addClient(client);
        client = factory.getClientRepository().getClientByICN(icn);
        account.setType("premium");
        Notification<Boolean> notification = clientService.updateClientAccount(client.getId(), account);
        Assert.assertFalse(notification.hasErrors());
    }

    @Test
    public void removeClient() throws SQLException {
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggg");
        clientService.addClient(client);
        client = factory.getClientRepository().getClientByICN(icn);
        Notification<Boolean> notification = clientService.removeClient(client.getId());
        Assert.assertFalse(notification.hasErrors());
    }

    @Test
    public void transfer() throws SQLException {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        Bootstrapper b = new Bootstrapper(connection);
        b.execute(true);
        Long icn = UniqueLongGenerator.generateUniqueLong();
        Client client = new Client();
        client.setName("Vlad Buzan");
        client.setIdentityCardNumber(icn);
        client.setAddress("Dr ioan ratiu nr 87");
        client.setPersonalNumericalCode("12321BBgggga");
        ClientAccount account = new ClientAccount();
        account.setAmount(200.0);
        account.setType("regular");
        account.setDateOfCreation(DateTime.now());
        account.setIdentificationNumber(123123L);
        client.setAccount(account);
        clientService.addClient(client);
        client = factory.getClientRepository().getClientByICN(icn);
        icn = UniqueLongGenerator.generateUniqueLong();
        Client client2 = new Client();
        client2.setName("Vlad Buzan");
        client2.setIdentityCardNumber(icn);
        client2.setAddress("Dr ioan ratiu nr 87");
        client2.setPersonalNumericalCode("12ffffBBgggg");
        client2.setIdentityCardNumber(icn);
        account.setAmount(100.0);
        account.setIdentificationNumber(123432L);
        client2.setAccount(account);
        clientService.addClient(client2);
        client2 = factory.getClientRepository().getClientByICN(icn);
        Notification<Boolean> notification = clientService.transfer(client.getId(), client2.getId(), 100.0);
        Assert.assertFalse(notification.hasErrors());
    }

}