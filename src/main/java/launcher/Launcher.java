package launcher;

import java.sql.SQLException;

public class Launcher {
    public static void main(String[] args) throws SQLException {
        ComponentFactory componentFactory = ComponentFactory.instance(true);
        componentFactory.getLoginView().setVisible(true);

    }
}
