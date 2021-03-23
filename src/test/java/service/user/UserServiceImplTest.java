package service.user;

import database.Bootstrapper;
import database.DBConnectionFactory;
import launcher.ComponentFactory;
import model.User;
import model.validation.Notification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.security.PasswordEncrypter;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserServiceImplTest {


    private static UserService userService;

    @BeforeClass
    public static void setUp() throws Exception {
        new Bootstrapper(new DBConnectionFactory().getConnectionWrapper(true).getConnection()).execute(true);
        userService = ComponentFactory.instance(true).getUserService();
        ComponentFactory.instance(true).getAuthenticationService().login("admin", "admin");
    }

    @Test
    public void createUser() {
        User user = new User();
        user.setUsername("vladbuzan@gmail.com");
        user.setPassword("DSFGsbvcb123:!");
        Notification<Boolean> notification =
                userService.addUser(user);
        Assert.assertFalse(notification.hasErrors());
        Notification<User> userNotification = userService.getUser("vladbuzan@gmail.com");
        Assert.assertFalse(userNotification.hasErrors());
    }

    @Test
    public void updateUserPassword() throws NoSuchAlgorithmException {
        User user = new User();
        user.setUsername("ana@gmail.com");
        user.setPassword("ffAA112!!");
        Notification<Boolean> notification = userService.addUser(user);
        Notification<Boolean> notification1 = userService.updateUserPassword("ana@gmail.com", "aaaffAA12!!");
        Notification<User> userNotification = userService.getUser("ana@gmail.com");
        Assert.assertFalse(notification.hasErrors());
        Assert.assertFalse(notification1.hasErrors());
        Assert.assertFalse(userNotification.hasErrors());
        Assert.assertEquals(userNotification.getResult().getPassword(), PasswordEncrypter.encrypt("aaaffAA12!!"));
    }

    @Test
    public void updateUserUsername() {
        User user = new User();
        user.setUsername("andreea@gmail.com");
        user.setPassword("Parol11@@ff");
        Notification<Boolean> notification = userService.addUser(user);
        Notification<Boolean> notification1 = userService.updateUserUsername("andreea@gmail.com",
                "Andreea@gmail.com");
        Assert.assertFalse(notification.hasErrors());
        Assert.assertFalse(notification1.hasErrors());
        Notification<User> notification2 = userService.getUser("Andreea@gmail.com");
        Assert.assertFalse(notification2.hasErrors());
        Assert.assertEquals("Andreea@gmail.com",
                notification2.getResult().getUsername());
    }

    @Test
    public void getUser() {
        User user = new User();
        user.setUsername("andrei@gamil.com");
        user.setPassword("DDDAA11122ss!");
        Notification<Boolean> registerNotification = userService.addUser(user);
        Assert.assertFalse(registerNotification.hasErrors());
        Notification<User> userNotification = userService.getUser("andrei@gamil.com");
        Assert.assertFalse(userNotification.hasErrors());
        Assert.assertEquals("andrei@gamil.com", userNotification.getResult().getUsername());
    }

    @Test
    public void getAll(){
        Notification<List<User>> notification = userService.getAll();
        Assert.assertFalse(notification.hasErrors());
        Assert.assertTrue(notification.getResult().size()>0);
    }

    @Test
    public void removeUser() {
        User user = new User();
        user.setUsername("andrei22@gamil.com");
        user.setPassword("DDDAA11122ss!");
        Notification<Boolean> registerNotification = userService.addUser(user);
        Assert.assertFalse(registerNotification.hasErrors());
        Notification<Boolean> notification = userService.removeUser(user.getUsername());
        Assert.assertFalse(notification.hasErrors());
    }







}