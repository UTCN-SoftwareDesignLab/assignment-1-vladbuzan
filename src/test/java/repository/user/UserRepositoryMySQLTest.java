package repository.user;

import database.Bootstrapper;
import database.Constants;
import database.JDBConnectionWrapper;
import model.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserRepositoryMySQLTest {
    private static Connection connection;
    private static UserRepository userRepository;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        connection = new JDBConnectionWrapper(Constants.Schemas.TEST).getConnection();
        Bootstrapper b = new Bootstrapper(connection);
        userRepository = new UserRepositoryMySQL(connection);
        b.execute(true);
    }

    @Test
    public void testAddUser() throws SQLException {
        userRepository.addUser("Vlad", "somePassword", "admin");
        User user = userRepository.getUserByUsername("Vlad");
        Assert.assertNotNull(user);
        Assert.assertEquals("Vlad", user.getUsername());
        Assert.assertEquals("somePassword", user.getPassword());
    }

    @Test
    public void testRemoveUser() throws SQLException {
        userRepository.addUser("Andrei", "ParolaMea", "admin");
        User user = userRepository.getUserByUsername("Andrei");
        Assert.assertNotNull(user);
        userRepository.removeUser("Andrei");
        Assert.assertNull(userRepository.getUserByUsername("Andrei"));
    }

    @Test
    public void testRemoveAll() throws SQLException {
        userRepository.removeAll();
        Assert.assertEquals(0, userRepository.getAll().size());
    }

    @Test
    public void testUpdateUsername() throws SQLException {
        userRepository.addUser("Daniel", "oParolaInteresanta", "admin");
        userRepository.updateUsername("Daniel", "DanielNou");
        Assert.assertNull(userRepository.getUserByUsername("Daniel"));
        User user = userRepository.getUserByUsername("DanielNou");
        Assert.assertNotNull(user);
        Assert.assertEquals("DanielNou", user.getUsername());
    }

    @Test
    public void testUpdatePassword() throws SQLException {
        userRepository.addUser("Marian", "MarianParola", "user");
        userRepository.updatePassword("Marian", "MarianParola2");
        User user = userRepository.getUserByUsername("Marian");
        Assert.assertEquals("MarianParola2", user.getPassword());
    }

    @Test
    public void testUpdateRole() throws SQLException {
        userRepository.addUser("VladBuz", "Parola", "user");
        userRepository.updateRole("VladBuz", "admin");
        User user = userRepository.getUserByUsername("VladBuz");
        Assert.assertEquals("admin", user.getRole().getRole());
    }

    @Test
    public void testGetUserById() throws SQLException {
        userRepository.addUser("Adrian", "Peculea", "admin");
        User user = userRepository.getUserByUsername("Adrian");
        User user2 = userRepository.getUserById(user.getId());
        Assert.assertEquals(user.getId(), user2.getId());
    }

    @Test
    public void testGetUserByUsername() throws SQLException {
        userRepository.addUser("Marcel", "parolaMarcel", "admin");
        User user = userRepository.getUserByUsername("Marcel");
        Assert.assertEquals("Marcel", user.getUsername());
    }

    @Test
    public void testGetUsersByRole() throws SQLException {
        userRepository.removeAll();
        userRepository.addUser("Donnie", "ParolaluiDonnie", "admin");
        userRepository.addUser("Nicusor", "parolaluiNicusor", "user");
        userRepository.addUser("Nicusor2", "parolaluiNicusor", "user");
        List<User> users = userRepository.getUsersByRole("user");
        Assert.assertEquals(2, users.size());
    }
}