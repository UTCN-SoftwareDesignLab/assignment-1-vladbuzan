package service.user;

import database.Bootstrapper;
import database.DBConnectionFactory;
import launcher.ComponentFactory;
import model.validation.Notification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;

public class AuthenticationServiceImplTest {

    private static AuthenticationService auth;
    private static ComponentFactory factory = ComponentFactory.instance(true);
    private static UserSession session;


    @BeforeClass
    public static void setUp() throws Exception {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        Bootstrapper b = new Bootstrapper(connection);
        b.execute(true);
        auth = factory.getAuthenticationService();
        session = factory.getUserSession();
    }

    @Test
    public void register() {
        Notification<Boolean> notification = auth.register("vldBuzan@gmail.com", "Parola`123~~");
        Assert.assertFalse(notification.hasErrors());
    }

    @Test
    public void login() {
        auth.register("vldBuzan2@gmail.com", "Parola`123~~");
        Notification<Boolean> notification = auth.login("vldBuzan2@gmail.com","Parola`123~~");
        Assert.assertFalse(notification.hasErrors());
        Assert.assertTrue(session.isSessionActive());
    }

    @Test
    public void logout() {
        auth.register("vldBuzan3@gmail.com", "Parola`123~~");
        auth.login("vldBuzan3@gmail.com","Parola`123~~");
        auth.logout(session.getLoggedInUser());
        Assert.assertFalse(session.isSessionActive());
    }
}