package service.user;

import database.Bootstrapper;
import database.DBConnectionFactory;
import launcher.ComponentFactory;
import model.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserSessionImplTest {

    private static UserSession userSession;
    private static User user;
    @BeforeClass
    public static void setUp() throws Exception {
        new Bootstrapper(new DBConnectionFactory().getConnectionWrapper(true).getConnection()).execute(true);
        userSession = ComponentFactory.instance(true).getUserSession();
        ComponentFactory.instance(true).getAuthenticationService().login("admin", "admin");
        user = userSession.getLoggedInUser();
    }
    @Test
    public void getLoggedInUser() {
        Assert.assertEquals("admin", userSession.getLoggedInUser().getUsername());
    }

    @Test
    public void startSession() {
        userSession.endSession();
        userSession.startSession(user);
        Assert.assertEquals("admin", userSession.getLoggedInUser().getUsername());
    }

    @Test
    public void endSession() {
        userSession.endSession();
        Assert.assertFalse(userSession.isSessionActive());
        userSession.startSession(user);
    }

    @Test
    public void isSessionActive() {
        userSession.startSession(user);
        Assert.assertTrue(userSession.isSessionActive());
    }
}