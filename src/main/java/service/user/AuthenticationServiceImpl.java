package service.user;

import database.Constants;
import model.Action;
import model.Role;
import model.User;
import model.validation.Notification;
import model.validation.UserValidator;
import org.joda.time.DateTime;
import repository.action.ActionRepository;
import repository.security.PasswordEncrypter;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final ActionRepository actionRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final UserSession session;

    public AuthenticationServiceImpl(UserRepository userRepository, ActionRepository actionRepository,
                                     RightsRolesRepository rightsRolesRepository,
                                     UserSession userSession) {

        this.userRepository = userRepository;
        this.actionRepository = actionRepository;
        this.rightsRolesRepository = rightsRolesRepository;
        this.session = userSession;
    }

    @Override
    public Notification<Boolean> register(String username, String password) {
        Notification<Boolean> notification = new Notification<>();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);


        UserValidator userValidator = new UserValidator(user);
        if(userValidator.validate()) {
            // save user into the database
            try {
                userRepository.addUser(username, PasswordEncrypter.encrypt(password), Constants.Roles.USER);
                user = userRepository.getUserByUsername(username);
            } catch (Exception e) {
                notification.addError("Failed to add the user into the database.");
                notification.setResult(true);
                return notification;
            }
            // if successful, save action
            try {
                Action action = new Action();
                action.setTime(DateTime.now());
                action.setDescription("Registered " + username + ".");
                actionRepository.save(action, user);
                notification.setResult(true);
                return notification;
            } catch (SQLException throwables) {
                notification.addError("Couldn't save action.");
                return  notification;
            }
        } else {
            userValidator.getErrors().forEach(notification::addError);
            return notification;
        }
    }

    @Override
    public Notification<Boolean> login(String username, String password) {
        Notification<Boolean> notification = new Notification<>();
        User user;

        try {
            user = userRepository.getUserByUsername(username);
            if(user == null) {
                notification.addError("Invalid username");
                return notification;
            }
            Role role = rightsRolesRepository.getRoleByUserId(user.getId());
            user.setRole(role);
            if(PasswordEncrypter.encrypt(password).equals(user.getPassword())) {
                notification.setResult(true);
                Action action = new Action();
                action.setTime(DateTime.now());
                action.setDescription("Logged in " + username + ".");
                actionRepository.save(action, user);
                session.startSession(user);
                return notification;
            }
            notification.addError("Invalid password");
            notification.setResult(false);
            return notification;

        } catch (SQLException | NoSuchAlgorithmException throwables) {
            notification.addError("Invalid username.");
            notification.setResult(false);
            return notification;
        }
    }

    @Override
    public boolean logout(User user) {
        session.endSession();
        return session.isSessionActive();
    }
}
