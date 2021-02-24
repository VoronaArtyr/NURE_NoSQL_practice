package ua.nure.style.session;

import ua.nure.style.dao.ConnectionFactory;
import ua.nure.style.dao.DAOFactory;
import ua.nure.style.dao.DAOType;
import ua.nure.style.dao.IMyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CookieValidator {

    private final IMyDao dao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MYSQL);

    private SessionTracker sessionTracker;


    @Autowired
    public CookieValidator( SessionTracker sessionTracker) {
        this.sessionTracker = sessionTracker;
    }

    public void check(String cookie) throws BadCookieException {
        if (cookie == null) {
            throw new BadCookieException("Cookie is null");
        }

        if (sessionTracker.getIdByCookie(cookie) == null) {
            throw new BadCookieException("There is no such cookie in SessionTracker");
        }

        if (dao.getUser(sessionTracker.getIdByCookie(cookie)).isEmpty()) {
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
