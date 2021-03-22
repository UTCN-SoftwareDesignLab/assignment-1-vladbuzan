package service.user;

import model.User;

public interface UserSession {

    User getLoggedInUser();
    void startSession(User user);
    void endSession();
    boolean isSessionActive();

}
