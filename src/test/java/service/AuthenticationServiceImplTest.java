package service;

import launcher.ComponentFactory;
import model.validation.Notification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.user.AuthenticationService;

public class AuthenticationServiceImplTest {


    private static final String GOOD_USERNAME = "VladBuzan@gmail.com";
    private static final String GOOD_PASSWORD = "V12lls23!gg";
    private static final String BAD_USERNAME = "vladbuzan.gmail.com";
    private static final String BAD_PASSWORD = "1122VVVV";
    private static AuthenticationService service;

    @BeforeClass
    public static void setUp() throws Exception {
        service = ComponentFactory.instance(true).getAuthenticationService();
    }

    @Test
    public void register() {
        Notification<Boolean> notification_good = service.register(GOOD_USERNAME, GOOD_PASSWORD);
        Assert.assertFalse(notification_good.hasErrors());
        Notification<Boolean> notification_bad = service.register(BAD_USERNAME, BAD_PASSWORD);
        Assert.assertTrue(notification_bad.hasErrors());
    }

    @Test
    public void login() {
        //will simply fail if user already registered
        Notification<Boolean> notification_good = service.register(GOOD_USERNAME, GOOD_PASSWORD);
        Notification<Boolean> notificationUser = service.login(GOOD_USERNAME, GOOD_PASSWORD);
        Assert.assertFalse(notificationUser.hasErrors());
    }

    @Test
    public void logout() {
    }


}