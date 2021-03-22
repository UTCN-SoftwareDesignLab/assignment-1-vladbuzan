package database;


import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class DBConnectionFactoryTest {

    @Test
    public void testConnection() {
        try {
            Assert.assertTrue(new DBConnectionFactory().getConnectionWrapper(true).testConnection());
        } catch (SQLException throwables) {
            Assert.fail();
        }
    }
}