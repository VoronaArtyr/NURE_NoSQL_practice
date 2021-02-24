package ua.nure.style.session;

public class BadCookieException extends Exception {
    public BadCookieException() {
    }

    public BadCookieException(String message) {
        super(message);
    }

    public BadCookieException(Exception e) {
        super(e);
    }

}
