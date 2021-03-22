package database;

import static database.Constants.Tables.*;

public class SQLTableCreationFactory {
    public String getCreateSQLForTable(String table) {

        switch (table) {
            case USER:
                return "CREATE TABLE IF NOT EXISTS user (" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "username VARCHAR(200) NOT NULL, " +
                        "password VARCHAR(64) NOT NULL, " +
                        "role_id INT NOT NULL," +
                        "PRIMARY KEY (id), " +
                        "FOREIGN KEY (role_id) " +
                        "REFERENCES role(id) " +
                        "ON DELETE CASCADE " +
                        "ON UPDATE CASCADE, " +
                        "UNIQUE INDEX id_UNIQUE (id ASC), " +
                        "UNIQUE INDEX username_UNIQUE (username ASC));";
            case ROLE:
                return "CREATE TABLE IF NOT EXISTS role (" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "role VARCHAR(100) NOT NULL, " +
                        "PRIMARY KEY (id), " +
                        "UNIQUE INDEX id_UNIQUE (id ASC), " +
                        "UNIQUE INDEX role_UNIQUE (role ASC));";
            case RIGHT:
                return "CREATE TABLE IF NOT EXISTS `right` ( " +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "right_name VARCHAR(100) NOT NULL, " +
                        "PRIMARY KEY (id), " +
                        "UNIQUE INDEX id_UNIQUE (id ASC), " +
                        "UNIQUE INDEX right_UNIQUE (right_name ASC));";
            case ROLE_RIGHT:
                return "CREATE TABLE IF NOT EXISTS role_right ( " +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "role_id INT NOT NULL, " +
                        "right_id INT NOT NULL, " +
                        "PRIMARY KEY (id), " +
                        "UNIQUE INDEX id_UNIQUE (id ASC), " +
                        "INDEX role_id_idx (role_id ASC), " +
                        "INDEX right_id_idx (right_id ASC), " +
                        "CONSTRAINT role_id " +
                        "  FOREIGN KEY (role_id) " +
                        "  REFERENCES role (id) " +
                        "  ON DELETE CASCADE " +
                        "  ON UPDATE CASCADE, " +
                        "CONSTRAINT right_id " +
                        "  FOREIGN KEY (right_id) " +
                        "  REFERENCES `right` (id) " +
                        "  ON DELETE CASCADE" +
                        "  ON UPDATE CASCADE);";
            case CLIENT:
                return "CREATE TABLE IF NOT EXISTS client ( " +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "name VARCHAR(200) NOT NULL, " +
                        "identity_card_number BIGINT(19) NOT NULL, " +
                        "personal_numerical_code VARCHAR(100) NOT NULL, " +
                        "address VARCHAR(200) NOT NULL, " +
                        "UNIQUE INDEX idcn_UNIQUE (identity_card_number ASC), " +
                        "PRIMARY KEY (id));";
            case CLIENT_ACCOUNT:
                return "CREATE TABLE IF NOT EXISTS client_account ( " +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "client_id INT NOT NULL," +
                        "type VARCHAR(100) NOT NULL, " +
                        "identification_number BIGINT(19) NOT NULL, " +
                        "amount DOUBLE NOT NULL, " +
                        "date_of_creation DATETIME NOT NULL, " +
                        "UNIQUE INDEX idn_UNIQUE (identification_number ASC), " +
                        "FOREIGN KEY (client_id) " +
                        "REFERENCES client(id) " +
                        "ON DELETE CASCADE " +
                        "ON UPDATE CASCADE, " +
                        "PRIMARY KEY (id))";
            case ACTION:
                return "CREATE TABLE IF NOT EXISTS action ( " +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "description VARCHAR(300) NOT NULL, " +
                        "time DATETIME NOT NULL, " +
                        "user_id INT NOT NULL, " +
                        "PRIMARY KEY (id), " +
                        "UNIQUE INDEX id_UNIQUE (id ASC), " +
                        "INDEX  user_id_idx (user_id ASC), " +
                        "CONSTRAINT user_id " +
                        "  FOREIGN KEY (user_id) " +
                        "  REFERENCES user (id) " +
                        "  ON DELETE CASCADE " +
                        "  ON UPDATE CASCADE);";
            case TRANSFER:
                return "CREATE TABLE IF NOT EXISTS transfer ( " +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "user_id INT NOT NULL, " +
                        "sender_id INT NOT NULL, " +
                        "receiver_id INT NOT NULL," +
                        "amount DOUBLE NOT NULL, " +
                        "PRIMARY KEY (id), " +
                        "INDEX user_id_idx (user_id ASC), " +
                        "INDEX sender_id_idx (sender_id ASC), " +
                        "CONSTRAINT user__id " +
                        "  FOREIGN KEY (user_id) " +
                        "  REFERENCES user (id) " +
                        "  ON DELETE CASCADE " +
                        "  ON UPDATE CASCADE, " +
                        "CONSTRAINT sender_id " +
                        "  FOREIGN KEY (sender_id) " +
                        "  REFERENCES client(id) " +
                        "  ON UPDATE CASCADE " +
                        "  ON DELETE CASCADE," +
                        "CONSTRAINT receiver_id " +
                        "  FOREIGN KEY (receiver_id) " +
                        "  REFERENCES client(id) " +
                        "  ON UPDATE CASCADE " +
                        "  ON DELETE CASCADE);";


            default:
                throw new IllegalStateException("Unexpected value: " + table);
        }
    }
}
