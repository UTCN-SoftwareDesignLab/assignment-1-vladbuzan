package launcher;

import controller.AdminController;
import controller.LoginController;
import controller.TransferController;
import controller.UserController;
import database.DBConnectionFactory;
import repository.action.ActionRepository;
import repository.action.ActionRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.client.ClientService;
import service.client.ClientServiceImpl;
import service.user.*;
import view.AdminView;
import view.LoginView;
import view.TransferView;
import view.UserView;

import java.sql.Connection;

public class ComponentFactory {

    private final ActionRepository actionRepository;

    private final RightsRolesRepository rightsRolesRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserSession userSession;
    private final ClientService clientService;

    private final LoginController loginController;
    private final LoginView loginView;

    private final AdminController adminController;
    private final AdminView adminView;

    private final UserController userController;
    private final UserView userView;

    private final TransferController transferController;
    private final TransferView transferView;

    private static ComponentFactory instance;


    private ComponentFactory(Boolean componentsForTests) {
        Connection connection = new DBConnectionFactory().
                getConnectionWrapper(componentsForTests).getConnection();

        actionRepository = new ActionRepositoryMySQL(connection);

        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection);
        clientRepository = new ClientRepositoryMySQL(connection);

        userSession = new UserSessionImpl();

        authenticationService = new AuthenticationServiceImpl(userRepository, actionRepository, rightsRolesRepository,
                userSession);
        userService = new UserServiceImpl(userRepository, userSession, actionRepository);
        clientService = new ClientServiceImpl(clientRepository, actionRepository, userSession);

        loginView = new LoginView();
        loginController = new LoginController(loginView, authenticationService);

        adminView = new AdminView();
        adminController = new AdminController(userSession, userService, adminView);

        userView = new UserView();
        userController = new UserController(clientService, userSession, userView);

        transferView = new TransferView();
        transferController = new TransferController(clientService, userSession, transferView);

        adminController.addObserver(loginController);
        userController.addObserver(transferController);
        loginController.addObserver(adminController);
        userController.addObserver(loginController);
        loginController.addObserver(userController);
        transferController.addObserver(userController);

    }

    public static ComponentFactory instance(Boolean componentsForTests) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTests);
        }
        return instance;
    }

    public ActionRepository getActionRepository() {
        return actionRepository;
    }



    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }


    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public AdminController getAdminController() {
        return adminController;
    }

    public AdminView getAdminView() {
        return adminView;
    }

    public static ComponentFactory getInstance() {
        return instance;
    }

    public UserController getUserController() {
        return userController;
    }

    public UserView getUserView() {
        return userView;
    }

    public TransferController getTransferController() {
        return transferController;
    }

    public TransferView getTransferView() {
        return transferView;
    }

}
