package service.user;

import model.User;

public class UserSessionImpl implements UserSession{

    private User user = null;
    private  boolean active = false;

    @Override
    public User getLoggedInUser() {
        return user;
    }

    @Override
    public void startSession(User user) {
        active = true;
        this.user = user;
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
