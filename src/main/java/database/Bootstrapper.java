package database;

import repository.security.PasswordEncrypter;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Bootstrapper {

    private final Connection connection;
    private RightsRolesRepository rightsRolesRepository;
    private UserRepository userRepository;

    public Bootstrapper(Connection connection){
        this.connection = connection;
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection);
    }
    public void execute(boolean dropExistingTables) throws SQLException {
        if(dropExistingTables) {
            dropAll();
        }
        bootstrapTables();
        try {
            userRepository.addUser("admin", PasswordEncrypter.encrypt("admin"), "admin");
            userRepository.addUser("user", PasswordEncrypter.encrypt("user"), "user");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private void dropAll() throws SQLException {
        Statement statement = connection.createStatement();
        String[] dropStatements = {
                "DROP TABLE IF EXISTS `role_right`;",
                "DROP TABLE IF EXISTS `right`;",
                "DROP TABLE IF EXISTS `transfer`;",
                "DROP TABLE IF EXISTS `action`;",
                "DROP TABLE IF EXISTS `user`;",
                "DROP TABLE IF EXISTS `role`;",
                "DROP TABLE IF EXISTS `client_info`;",
                "DROP TABLE IF EXISTS `client_account`;",
                "DROP TABLE IF EXISTS `client`;"

        };
        for (String st : dropStatements) {
            System.out.println("Executing " + st);
            statement.execute(st);
        }
    }

    private void bootstrapTables() throws SQLException {
        SQLTableCreationFactory sqlTableCreationFactory = new SQLTableCreationFactory();
        Statement statement = connection.createStatement();

        for (String table : Constants.Tables.ORDERED_TABLES_FOR_CREATION) {
            String createTableStatement = sqlTableCreationFactory.getCreateSQLForTable(table);
            System.out.println("Executing " + createTableStatement);
            statement.execute(createTableStatement);
        }
        bootstrapRoles();
        bootstrapRoleRights();
    }

    private void bootstrapRoles() throws SQLException {
        for (String role : Constants.Roles.ROLES) {
            rightsRolesRepository.addRole(role);
        }
    }

    private void bootstrapRoleRights() throws SQLException {
        for(String role : Constants.getRolesRights().keySet()) {
            for(String right : Constants.getRolesRights().get(role)) {
                rightsRolesRepository.addRoleRight(role, right);
            }
        }
    }
}
