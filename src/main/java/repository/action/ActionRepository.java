package repository.action;

import model.Action;
import model.User;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.List;

public interface ActionRepository {

    List<Action> getAll() throws SQLException;
    List<Action> getBetween(DateTime start, DateTime end) throws SQLException;
    List<Action> getByUser(Long userId) throws SQLException;
    void save(Action action, User user) throws SQLException;
    void removeAll() throws SQLException;

}
