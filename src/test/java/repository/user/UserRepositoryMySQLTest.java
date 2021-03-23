package repository.user;

import database.Bootstrapper;
import database.Constants;
import database.JDBConnectionWrapper;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserRepositoryMySQLTest {
    private static Connection connection;
    private static UserRepository userRepository;
    private static final String USERNAME = "vladBuzan@gmail.com";
    private static final String NEW_USERNAME = "vlad_Buzan@yahoo.com";
    private static final String PASSWORD = "vladBuzan!!1122";
    private static final String NEW_PASSWORD = "newPassword!!111";
    private static final String ROLE = "admin";
    private static final String NEW_ROLE = "user";

    @BeforeClass
    public static void setUpClass() throws SQLException {
        connection = new JDBConnectionWrapper(Constants.Schemas.TEST).getConnection();
        Bootstrapper b = new Bootstrapper(connection);
        userRepository = new UserRepositoryMySQL(connection);
        b.execute(true);
    }

    @Before
    public void before() throws SQLException {
        userRepository.removeAll();
    }

    @Test
    public void testAddUser() throws SQLException {
        userRepository.addUser(USERNAME, PASSWORD, ROLE);
        User user = userRepository.getUserByUsername(USERNAME);
        Assert.assertNotNull(user);
        Assert.assertEquals(USERNAME, user.getUsername());
        Assert.assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testRemoveUser() throws SQLException {
        userRepository.addUser(USERNAME, PASSWORD, ROLE);
        User user = userRepository.getUserByUsername(USERNAME);
        Assert.assertNotNull(user);
        userRepository.removeUser(USERNAME);
        Assert.assertNull(userRepository.getUserByUsername(USERNAME));
    }

    @Test
    public void testRemoveAll() throws SQLException {
        userRepository.removeAll();
        Assert.assertEquals(0, userRepository.getAll().size());
    }

    @Test
    public void testUpdateUsername() throws SQLException {
        userRepository.addUser(USERNAME, PASSWORD, ROLE);
        userRepository.updateUsername(USERNAME, NEW_USERNAME);
        Assert.assertNull(userRepository.getUserByUsername(USERNAME));
        User user = userRepository.getUserByUsername(NEW_USERNAME);
        Assert.assertNotNull(user);
        Assert.assertEquals(NEW_USERNAME, user.getUsername());
    }

    @Test
    public void testUpdatePassword() throws SQLException {
        userRepository.addUser(USERNAME, PASSWORD, ROLE);
        userRepository.updatePassword(USERNAME, NEW_PASSWORD);
        User user = userRepository.getUserByUsername(USERNAME);
        Assert.assertEquals(NEW_PASSWORD, user.getPassword());
    }

    @Test
    public void testUpdateRole() throws SQLException {
        userRepository.addUser(USERNAME, PASSWORD, ROLE);
        userRepository.updateRole(USERNAME, NEW_ROLE);
        User user = userRepository.getUserByUsername(USERNAME);
        Assert.assertEquals(NEW_ROLE, user.getRole().getRole());
    }

    @Test
    public void testGetUserById() throws SQLException {
        userRepository.addUser(USERNAME, PASSWORD, ROLE);
        User user = userRepository.getUserByUsername(USERNAME);
        User user2 = userRepository.getUserById(user.getId());
        Assert.assertEquals(user.getId(), user2.getId());
    }

    @Test
    public void testGetUserByUsername() throws SQLException {
        userRepository.addUser(USERNAME, PASSWORD, ROLE);
        User user = userRepository.getUserByUsername(USERNAME);
        Assert.assertEquals(USERNAME, user.getUsername());
    }

    @Test
    public void testGetUsersByRole() throws SQLException {
        userRepository.addUser(USERNAME, PASSWORD, ROLE);
        userRepository.addUser(USERNAME + "2", PASSWORD, ROLE);
        userRepository.addUser(USERNAME + "3", PASSWORD, NEW_ROLE);
        List<User> users = userRepository.getUsersByRole(ROLE);
        Assert.assertEquals(2, users.size());
    }
}