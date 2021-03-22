package repository.action;

import model.Action;
import model.User;
import org.joda.time.DateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActionRepositoryMySQL implements ActionRepository{

    private final Connection connection;

    public ActionRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Action> getAll() throws SQLException {
        List<Action> actions = new ArrayList<>();
        Statement statement = connection.createStatement();
        String fetchActionsSQL = "SELECT * FROM action ;";
        ResultSet resultSet = statement.executeQuery(fetchActionsSQL);
        System.out.println(resultSet);
        while(resultSet.next()){
            actions.add(getActionFromResultSet(resultSet));
        }
        return actions;
    }

    @Override
    public List<Action> getBetween(DateTime start, DateTime end) throws SQLException {
        List<Action> actions = new ArrayList<>();
        java.sql.Date startDate = new java.sql.Date(start.toDate().getTime());
        java.sql.Date endDate = new java.sql.Date(end.toDate().getTime());
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT * FROM action " +
                "WHERE time BETWEEN ? AND ? ;");
        preparedStatement.setDate(1, startDate);
        preparedStatement.setDate(2, endDate);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            actions.add(getActionFromResultSet(resultSet));
        }
        return actions;
    }

    @Override
    public List<Action> getByUser(Long userId) throws SQLException {
        List<Action> actions = new ArrayList<>();
        Statement statement = connection.createStatement();
        String fetchActionsSQL = "SELECT * FROM action " +
                "WHERE user_id = " + userId.toString() + " ;";
        ResultSet resultSet = statement.executeQuery(fetchActionsSQL);
        while(resultSet.next()){
            actions.add(getActionFromResultSet(resultSet));
        }
        return actions;
    }

    @Override
    public void save(Action action, User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO action (description, time, user_id) " +
                        "VALUES (?, ?, ?); "
        );
        preparedStatement.setString(1, action.getDescription());
        preparedStatement.setDate(2, new Date(action.getTime().getMillis()));
        preparedStatement.setLong(3, user.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void removeAll() throws SQLException {
        Statement statement = connection.createStatement();
        String removeSQL = "DELETE FROM action " +
                "WHERE id >= 0 ;";
        statement.executeUpdate(removeSQL);
    }

    private Action getActionFromResultSet(ResultSet resultSet) throws SQLException {
        Action action = new Action();
        action.setDescription(resultSet.getString("description"));
        action.setTime(new DateTime(resultSet.getDate("time").getTime()));
        return action;
    }
}
