package repository.security;

import database.Bootstrapper;
import database.Constants;
import database.JDBConnectionWrapper;
import model.Role;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RightsRolesRepositoryMySQLTest {
    private static Connection connection;
    private static RightsRolesRepository repository;
    private static final String USERNAME = "vladBuzan@gmail.com";
    private static final String PASSWORD = "parola!22AA";
    private static final String ADMIN = "admin";
    private static final String USER = "user";
    private static final String NEW_ROLE = "new role";
    private static final String NEW_RIGHT = "new right";

    @BeforeClass
    public static void setUpClass() throws SQLException{
        connection = new JDBConnectionWrapper(Constants.Schemas.TEST).getConnection();
        Bootstrapper b = new Bootstrapper(connection);
        repository = new RightsRolesRepositoryMySQL(connection);
        b.execute(true);
        new UserRepositoryMySQL(connection).addUser(USERNAME, PASSWORD, ADMIN);
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void addRole() throws SQLException {
        repository.addRole(NEW_ROLE);
        Assert.assertEquals(NEW_ROLE, repository.getRoleByTitle(NEW_ROLE).getRole());
    }

    @Test
    public void addRight() throws SQLException {
        repository.addRight(NEW_RIGHT);
        Assert.assertEquals(NEW_RIGHT, repository.getRightByTitle(NEW_RIGHT).getRightName());
    }

    @Test
    public void addRoleRight() throws SQLException {
        repository.addRoleRight(ADMIN, NEW_RIGHT);
        User user = new UserRepositoryMySQL(connection).getUserByUsername(USERNAME);
        List<String> rights = new ArrayList<>();
        user.getRole().getRights().forEach(e -> rights.add(e.getRightName()));
        Assert.assertTrue(rights.contains(NEW_RIGHT));
    }


    @Test
    public void getRoleByUserId() throws SQLException {
        User user = new UserRepositoryMySQL(connection).getUserByUsername(USERNAME);
        Role role = repository.getRoleByUserId(user.getId());
        Assert.assertEquals(ADMIN, role.getRole());
    }

    @Test
    public void getRoleByTitle() throws SQLException {
        Assert.assertEquals(ADMIN, repository.getRoleByTitle(ADMIN).getRole());
    }

    @Test
    public void getRightByTitle() throws SQLException {
        repository.addRight(NEW_RIGHT);
        Assert.assertEquals(NEW_RIGHT, repository.getRightByTitle(NEW_RIGHT).getRightName());
    }
}