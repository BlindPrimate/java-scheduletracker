package scheduler.services;

import scheduler.models.User;

public class Authenticator {

    private Authenticator authenticator;
    private User user;

    private Authenticator() {

    }


    public void setUser(int userID, String password) {
        this.user = new User(userID, password);
    }

    public Authenticator getInstance() {
        if (authenticator == null) {
            authenticator = new Authenticator();
            return authenticator;
        }
        return authenticator;
    }
}
