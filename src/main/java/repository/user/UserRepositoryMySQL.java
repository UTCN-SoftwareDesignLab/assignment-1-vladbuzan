package repository.user;

import model.Role;
import model.User;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryMySQL implements UserRepository{

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public UserRepositoryMySQL(Connection connection) {
        this.connection = connection;
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
    }

    @Override
    public void addUser(String username, String password, String roleTitle) throws SQLException {
        rightsRolesRepository.addRole(roleTitle);
        Role role = rightsRolesRepository.getRoleByTitle(roleTitle);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO `user` (username, password, role_id) " +
                        "VALUES (?, ?, ?);"
        );
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setLong(3, role.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void removeUser(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String deleteSQL = "DELETE FROM user " +
                "WHERE user.username = '" + username + "' ;";
        statement.executeUpdate(deleteSQL);

    }

    @Override
    public void removeAll() throws SQLException {
        Statement statement = connection.createStatement();
        String truncateSQL = "DELETE FROM user " +
                "WHERE user.id >= 0;";
        statement.execute(truncateSQL);
    }

    @Override
    public void updateUsername(String oldUsername, String newUsername) throws SQLException {
        Statement statement = connection.createStatement();
        String updateSQL = "UPDATE user " +
                "SET username = '" + newUsername + "' " +
                "WHERE `user`.username = '" + oldUsername + "';";
        statement.executeUpdate(updateSQL);
    }

    @Override
    public void updatePassword(String username, String newPassword) throws SQLException {
        Statement statement = connection.createStatement();
        String updateSQL = "UPDATE user " +
                "SET password = '" + newPassword + "' " +
                "WHERE `user`.username = '" + username + "';";
        statement.executeUpdate(updateSQL);
    }

    @Override
    public void updateRole(String username, String newRole) throws SQLException {
        rightsRolesRepository.addRole(newRole);
        Long roleId = rightsRolesRepository.getRoleByTitle(newRole).getId();
        System.out.println(roleId);
        Statement statement = connection.createStatement();
        String updateSQL = "UPDATE user " +
                "SET role_id = " + roleId + " " +
                "WHERE `user`.username = '" + username + "';";
        statement.executeUpdate(updateSQL);
    }

    @Override
    public User getUserById(Long id) throws SQLException {
        Statement statement = connection.createStatement();
        String fetchUserSQL = "SELECT * FROM `user` " +
                "WHERE user.id = " + id.toString() + ";";
        ResultSet resultSet = statement.executeQuery(fetchUserSQL);
        if(resultSet.next()){
            return getUserFromResultSet(resultSet);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String fetchUserSQL = "SELECT * FROM `user` " +
                "WHERE user.username = '" + username + "';";
        ResultSet resultSet = statement.executeQuery(fetchUserSQL);
        if(resultSet.next()){
            return getUserFromResultSet(resultSet);
        } else {
            return null;
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        Statement statement = connection.createStatement();
        String fetchAllSQL = "SELECT * FROM user;";
        ResultSet resultSet = statement.executeQuery(fetchAllSQL);
        while(resultSet.next()){
            users.add(getUserFromResultSet(resultSet));
        }
        return users;
    }

    @Override
    public List<User> getUsersByRole(String roleTitle) throws SQLException {
        List<User> users = new ArrayList<>();
        Statement statement = connection.createStatement();
        String fetchUsersSQL = "SELECT user.id, user.username, user.password, user.role_id " +
                "FROM `user` INNER JOIN `role` " +
                "ON user.role_id = role.id " +
                "WHERE role.role = '" + roleTitle + "';";
        ResultSet resultSet = statement.executeQuery(fetchUsersSQL);
        while(resultSet.next()){
            users.add(getUserFromResultSet(resultSet));
        }
        return users;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(rightsRolesRepository.getRoleByUserId(user.getId()));
        return user;
    }
}
