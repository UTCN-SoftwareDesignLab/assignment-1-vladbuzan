package repository.user;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    void addUser(String username, String password, String roleTitle) throws SQLException;
    void removeUser(String username) throws SQLException;
    void removeAll() throws SQLException;
    void updateUsername(String oldUsername, String newUsername) throws SQLException;
    void updatePassword(String username, String newPassword) throws SQLException;
    void updateRole(String username, String newRole) throws SQLException;
    User getUserById(Long id) throws SQLException;
    User getUserByUsername(String username) throws SQLException;
    List<User> getAll() throws SQLException;
    List<User> getUsersByRole(String roleTitle) throws SQLException;
}
