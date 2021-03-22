package service.user;

import model.User;
import model.validation.Notification;
import org.joda.time.DateTime;

import java.util.List;

public interface UserService {

    Notification<Boolean> addUser(User user);
    Notification<Boolean> updateUserPassword(String username, String newPassword);
    Notification<Boolean> updateUserUsername(String username, String newUsername);
    Notification<User> getUser(String username);
    Notification<List<User>> getAll();
    Notification<Boolean> removeUser(String username);
    Notification<Boolean> generateReport(DateTime start, DateTime end, String username);

}
