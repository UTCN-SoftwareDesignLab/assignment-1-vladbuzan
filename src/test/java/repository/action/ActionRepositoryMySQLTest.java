package repository.action;

import database.Bootstrapper;
import database.Constants;
import database.JDBConnectionWrapper;
import model.Action;
import model.User;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ActionRepositoryMySQLTest {
    private static Connection connection;
    private static ActionRepository repository;
    private static final String ACTION = "Some generic action";
    private static User user;
    private static final String USERNAME = "vladBuzan@gmail.com";
    private static final String PASSWORD = "vladBuzan!!1122";
    private static final String ROLE = "admin";

    @BeforeClass
    public static void setupClass() throws SQLException {
        connection = new JDBConnectionWrapper(Constants.Schemas.TEST).getConnection();
        Bootstrapper b = new Bootstrapper(connection);
        repository = new ActionRepositoryMySQL(connection);
        b.execute(true);
        UserRepositoryMySQL userRepositoryMySQL = new UserRepositoryMySQL(connection);
        userRepositoryMySQL.addUser(USERNAME, PASSWORD, ROLE);
        user = userRepositoryMySQL.getUserByUsername(USERNAME);
    }

    @Before
    public void setUp() throws Exception {
        repository.removeAll();
    }

    @Test
    public void getAll() throws SQLException {
        Action action = new Action();
        action.setTime(DateTime.now());
        action.setDescription(ACTION);
        repository.save(action, user);
        action.setDescription(ACTION + " 2");
        action.setTime(DateTime.now());
        repository.save(action, user);
        Assert.assertTrue(repository.getAll().size() == 2);
    }

    @Test
    public void getBetween() throws SQLException {
        DateTime now = DateTime.now();
        DateTime end = now.plusDays(24);
        DateTime outside = end.plusDays(2);
        Action action = new Action();
        action.setDescription(ACTION);
        action.setTime(now);
        repository.save(action, user);
        action.setTime(end);
        repository.save(action, user);
        action.setTime(outside);
        repository.save(action, user);
        Assert.assertTrue(repository.getBetween(now, end).size() == 2);
    }

    @Test
    public void getByUser() throws SQLException {
        Action action = new Action();
        action.setDescription(ACTION);
        action.setTime(DateTime.now());
        repository.save(action, user);
        Assert.assertEquals(ACTION, repository.getByUser(user.getId()).get(0).getDescription());
    }

    @Test
    public void save() throws SQLException {
        Action action = new Action();
        action.setDescription(ACTION);
        action.setTime(DateTime.now());
        repository.save(action, user);
        Assert.assertEquals(1, repository.getAll().size());
    }

    @Test
    public void removeAll() throws SQLException {
        Action action = new Action();
        action.setDescription(ACTION);
        action.setTime(DateTime.now());
        repository.save(action, user);
        repository.removeAll();
        Assert.assertEquals(0, repository.getAll().size());
    }
}