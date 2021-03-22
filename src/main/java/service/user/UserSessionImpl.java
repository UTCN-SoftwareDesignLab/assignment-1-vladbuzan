package service.user;

import model.User;

public class UserSessionImpl implements UserSession{

    private static User user = null;
    private static boolean active = false;

    @Override
    public User getLoggedInUser() {
        return user;
    }

    @Override
    public void startSession(User user) {
        active = true;
        UserSessionImpl.user = user;
    }

    @Override
    public void endSession() {
        active = false;
        user = null;
    }

    @Override
    public boolean isSessionActive() {
        return active;
    }
}
