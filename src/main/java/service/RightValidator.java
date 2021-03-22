package service;

import model.Right;
import model.User;

public class RightValidator {
    public static boolean hasRight(User user, String requiredRight) {
        for(Right right : user.getRole().getRights()) {
            if(right.getRightName().equals(requiredRight))
                return true;
        }
        return false;
    }
}
