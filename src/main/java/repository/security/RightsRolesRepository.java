package repository.security;

import model.Right;
import model.Role;

import java.sql.SQLException;

public interface RightsRolesRepository {

    void addRole(String role) throws SQLException;
    void addRight(String right) throws SQLException;
    void addRoleRight(Long roleId, Long rightId) throws SQLException;
    void addRoleRight(String roleTitle, String rightTitle) throws SQLException;
    Role getRoleByUserId(Long userId) throws SQLException;
    Role getRoleByTitle(String roleTitle) throws SQLException;
    Right getRightByTitle(String rightTitle) throws SQLException;

}