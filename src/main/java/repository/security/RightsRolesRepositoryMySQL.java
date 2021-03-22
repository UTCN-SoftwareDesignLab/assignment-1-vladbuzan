package repository.security;

import model.Right;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ROLE;
import static database.Constants.Tables.ROLE_RIGHT;

public class RightsRolesRepositoryMySQL implements RightsRolesRepository {

    private final Connection connection;

    public RightsRolesRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRole(String role) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT IGNORE INTO " + ROLE + " (role) " +
                        " VALUES (?); ");
        preparedStatement.setString(1, role);
        preparedStatement.executeUpdate();
    }

    @Override
    public void addRight(String right) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT IGNORE INTO " + "`right`" + " (`right_name`) " +
                        " VALUES (?); "
        );
        preparedStatement.setString(1, right);
        preparedStatement.executeUpdate();
    }

    @Override
    public void addRoleRight(Long roleId, Long rightId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + ROLE_RIGHT + "(role_id, right_id) " +
                        " VALUES (?, ?);"
        );
        preparedStatement.setLong(1, roleId);
        preparedStatement.setLong(2, rightId);
        preparedStatement.executeUpdate();
    }

    @Override
    public void addRoleRight(String roleTitle, String rightTitle) throws SQLException {
        Role role = getRoleByTitle(roleTitle);
        addRight(rightTitle);
        Right right  = getRightByTitle(rightTitle);
        addRoleRight(role.getId(), right.getId());
    }

    @Override
    public Role getRoleByUserId(Long userId) throws SQLException {
        Statement statement = connection.createStatement();
        String selectStatement = "SELECT role.id, role.role " +
                "FROM user INNER JOIN role " +
                "ON user.role_id = role.id " +
                "WHERE user.id = " + userId.toString() + "; ";
        ResultSet resultSet = statement.executeQuery(selectStatement);
        if(resultSet.next()) {
            Role role = getRoleFromResultSet(resultSet);
            role.setRights(getRightsByRoleId(role.getId()));
            return role;
        } else {
            return null;
        }
    }

    @Override
    public Role getRoleByTitle(String roleTitle) throws SQLException {
        Statement statement = connection.createStatement();
        String fetchRoleSQL = "SELECT * FROM `role`" +
                "WHERE `role` = '" + roleTitle + "';";
        ResultSet resultSet = statement.executeQuery(fetchRoleSQL);
        if(resultSet.next()){
            Role role = getRoleFromResultSet(resultSet);
            role.setRights(getRightsByRoleId(role.getId()));
            return role;
        } else {
            return null;
        }
    }

    @Override
    public Right getRightByTitle(String rightTitle) throws SQLException {
        Statement statement = connection.createStatement();
        String fetchRoleSQL = "SELECT * FROM `right`" +
                "WHERE `right_name` = '" + rightTitle + "';";
        ResultSet resultSet = statement.executeQuery(fetchRoleSQL);
        if(resultSet.next()){
            return getRightFromResultSet(resultSet);
        } else {
            return null;
        }
    }

    private List<Right> getRightsByRoleId(Long id) throws SQLException {
        List<Right> rights = new ArrayList<>();
        Statement statement = connection.createStatement();
        String fetchRightsSQL = "SELECT `right`.id, `right`.right_name " +
                "FROM `right` INNER JOIN `role_right` " +
                "ON `right`.id = `role_right`.right_id " +
                "WHERE `role_right`.role_id = " + id.toString() + ";";
        ResultSet resultSet = statement.executeQuery(fetchRightsSQL);
        while(resultSet.next()) {
            rights.add(getRightFromResultSet(resultSet));
        }
        return rights;
    }
    private Role getRoleFromResultSet(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setRole(resultSet.getString("role"));
        role.setId(resultSet.getLong("id"));
        return role;
    }

    private Right getRightFromResultSet(ResultSet resultSet) throws SQLException {
        Right right = new Right();
        right.setRightName(resultSet.getString("right_name"));
        right.setId(resultSet.getLong("id"));
        return right;
    }

}
