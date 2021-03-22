package service.user;

import database.Constants;
import model.Action;
import model.User;
import model.validation.Notification;
import model.validation.UserValidator;
import org.joda.time.DateTime;
import repository.action.ActionRepository;
import repository.security.PasswordEncrypter;
import repository.user.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static service.RightValidator.hasRight;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserSession userSession;
    private final ActionRepository actionRepository;

    public UserServiceImpl(UserRepository userRepository, UserSession userSession, ActionRepository actionRepository) {
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.actionRepository = actionRepository;
    }

    @Override
    public Notification<Boolean> addUser(User user) {
        Notification<Boolean> notification = new Notification<>();

        if(!hasRight(userSession.getLoggedInUser(), Constants.Rights.CREATE_USER)) {
            notification.addError("No access privilege to perform this action.");
            notification.setResult(false);
            return notification;
        }



        UserValidator userValidator = new UserValidator(user);
        if(userValidator.validate()) {
            try {
                userRepository.addUser(user.getUsername(), PasswordEncrypter.encrypt(user.getPassword()), Constants.Roles.USER);
                notification.setResult(true);
                return notification;
            } catch (Exception e) {
                notification.addError("Couldn't add user.");
                notification.setResult(false);
                return notification;
            }
        }
        userValidator.getErrors().forEach(notification::addError);
        notification.setResult(false);
        return notification;
    }

    @Override
    public Notification<Boolean> updateUserPassword(String username, String newPassword) {
        Notification<Boolean> notification = new Notification<>();

        if(!hasRight(userSession.getLoggedInUser(), Constants.Rights.UPDATE_USER)) {
            notification.addError("No access privilege to perform this action.");
            notification.setResult(false);
            return notification;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(newPassword);
        UserValidator userValidator = new UserValidator(user);

        if(userValidator.validate()) {
            try {
                userRepository.updatePassword(username, PasswordEncrypter.encrypt(newPassword));
                notification.setResult(true);
                return notification;
            } catch (Exception e) {
                notification.addError("Couldn't update password.");
                notification.setResult(false);
                return notification;
            }
        }
        userValidator.getErrors().forEach(notification::addError);
        notification.setResult(false);
        return notification;
    }

    @Override
    public Notification<Boolean> updateUserUsername(String username, String newUsername) {
        Notification<Boolean> notification = new Notification<>();

        if(!hasRight(userSession.getLoggedInUser(), Constants.Rights.UPDATE_USER)) {
            notification.addError("No access privilege to perform this action.");
            return notification;
        }

        try {
            userRepository.updateUsername(username, newUsername);
            notification.setResult(true);
            return notification;

        } catch (SQLException throwables) {
            notification.addError("Error while querying the database.");
            return notification;
        }
    }

    @Override
    public Notification<User> getUser(String username) {
        Notification<User> notification = new Notification<>();

        if(!hasRight(userSession.getLoggedInUser(), Constants.Rights.READ_USER)) {
            notification.addError("No access privilege to perform this action.");
            return notification;
        }

        try {
            User user = userRepository.getUserByUsername(username);
            if(user == null) {
                notification.addError("No such user.");
                return notification;
            }
            notification.setResult(user);
            return notification;
        } catch (SQLException throwables) {
            notification.addError("Couldn't find the user.");
        }
        return notification;
    }

    @Override
    public Notification<List<User>> getAll() {
        Notification<List<User>> notification = new Notification<>();

        if(!hasRight(userSession.getLoggedInUser(), Constants.Rights.READ_USER)) {
            notification.addError("No access privilege to perform this action.");
            return notification;
        }

        try {
            notification.setResult(userRepository.getAll());
        } catch (SQLException throwables) {
            notification.addError("Error while querying the database.");
        }

        return notification;
    }

    @Override
    public Notification<Boolean> removeUser(String username) {
        Notification<Boolean> notification = new Notification<>();

        if(!hasRight(userSession.getLoggedInUser(), Constants.Rights.DELETE_USER)) {
            notification.addError("No access privilege to perform this action.");
            return notification;
        }

        try {
            userRepository.removeUser(username);
            notification.setResult(true);
            return notification;
        } catch (SQLException throwables) {
            notification.addError("Error while performing updates.");
            return notification;
        }
    }

    @Override
    public Notification<Boolean> generateReport(DateTime start, DateTime end, String username) {
        Notification<Boolean> notification = new Notification<>();
        try {
            User user = userRepository.getUserByUsername(username);
            List<Action> actions = actionRepository.getByUser(user.getId());
            List<Action> collect = actions.stream().filter(e -> ((e.getTime().getMillis() > start.getMillis()) &&
                    (e.getTime().getMillis() < end.getMillis()))).collect(Collectors.toList());
            collect.forEach(a -> System.out.println(a.getDescription() + " " + a.getTime().toString()));
        } catch (SQLException throwables) {
            notification.addError("SQL Error");
            return notification;
        }
        notification.setResult(true);
        return notification;
    }


}
