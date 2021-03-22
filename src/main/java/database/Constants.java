package database;

import java.util.*;

import static database.Constants.Rights.*;
import static database.Constants.Roles.ADMIN;
import static database.Constants.Roles.USER;

public class Constants {

    public static class Schemas {
        public static final String TEST = "test_banking";
        public static final String PRODUCTION = "production_banking";
    }

    public static class Tables {
        public static final String USER = "user";
        public static final String CLIENT = "client";
        public static final String CLIENT_ACCOUNT = "client_account";
        public static final String ROLE = "role";
        public static final String RIGHT = "right";
        public static final String ROLE_RIGHT = "role_right";
        public static final String ACTION = "action";
        public static final String TRANSFER = "transfer";
        public static final String[] ORDERED_TABLES_FOR_CREATION = {
                ROLE, USER, RIGHT, ROLE_RIGHT,CLIENT, CLIENT_ACCOUNT, ACTION, TRANSFER
        };

    }

    public static class ClientTypes {
        public static final String PREMIUM = "premium_client";
        public static final String REGULAR = "regular_client";
        public static final String COMPANY = "company_client";
    }

    public static class Roles {
        public static final String USER = "user";
        public static final String ADMIN = "admin";

        public static final String[] ROLES = new String[] {USER, ADMIN};
    }

    public static class Rights {

        public static final String CREATE_CLIENT = "create_client";
        public static final String READ_CLIENT = "read_client";
        public static final String UPDATE_CLIENT = "update_client";
        public static final String DELETE_CLIENT = "delete_client";
        public static final String TRANSFER_MONEY = "transfer_money";
        public static final String ADD_CLIENT_INFO = "add_client_info";
        public static final String READ_CLIENT_INFO = "read_client_info";
        public static final String UPDATE_CLIENT_INFO = "update_client_info";

        public static final String CREATE_USER = "create_user";
        public static final String READ_USER = "read_user";
        public static final String UPDATE_USER = "update_user";
        public static final String DELETE_USER = "delete_user";
        public static final String GENERATE_REPORT = "generate_report";

        public static final String[] RIGHTS = {CREATE_CLIENT, READ_CLIENT, UPDATE_CLIENT,
        DELETE_CLIENT, TRANSFER_MONEY, ADD_CLIENT_INFO, READ_CLIENT_INFO, UPDATE_CLIENT_INFO,
        CREATE_USER, READ_USER, UPDATE_USER, DELETE_USER, GENERATE_REPORT};
    }

    public static Map<String, List<String>> getRolesRights() {
        Map<String, List<String>> ROLES_RIGHTS = new HashMap<>();
        for(String role : Roles.ROLES) {
            ROLES_RIGHTS.put(role, new ArrayList<>());
        }
        ROLES_RIGHTS.get(USER).addAll(Arrays.asList(CREATE_CLIENT, READ_CLIENT, UPDATE_CLIENT, DELETE_CLIENT,
                TRANSFER_MONEY, ADD_CLIENT_INFO, READ_CLIENT_INFO, UPDATE_CLIENT_INFO));
        ROLES_RIGHTS.get(ADMIN).addAll(Arrays.asList(CREATE_USER, READ_USER, UPDATE_USER, DELETE_USER,
                GENERATE_REPORT));
        return ROLES_RIGHTS;
    }

}
