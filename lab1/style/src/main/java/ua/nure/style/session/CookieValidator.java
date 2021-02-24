package ua.nure.style.session;

import ua.nure.style.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CookieValidator {

    private UserDAO userDAO;
    private SessionTracker sessionTracker;


    @Autowired
    public CookieValidator(UserDAO userDAO,
                           SessionTracker sessionTracker) {
        this.userDAO = userDAO;
        this.sessionTracker = sessionTracker;
    }

    public void check(String cookie) throws BadCookieException {
        if (cookie == null) {
            throw new BadCookieException("Cookie is null");
        }

        if (sessionTracker.getIdByCookie(cookie) == null) {
            throw new BadCookieException("There is no such cookie in SessionTracker");
        }

        if (userDAO.get(sessionTracker.getIdByCookie(cookie)).isEmpty()) {
            throw new BadCookieException("User with such id not found");
        }

    }

    public boolean isCorrect(String cookie) {
        try {
            this.check(cookie);
            return true;
        } catch (BadCookieException e) {
            return false;
        }
    }
}
