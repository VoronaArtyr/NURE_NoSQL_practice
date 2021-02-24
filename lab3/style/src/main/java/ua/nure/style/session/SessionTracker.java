package ua.nure.style.session;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SessionTracker {
    private static final SessionTracker INSTANCE = new SessionTracker();
    private static final HashMap<String, Long> storage = new HashMap<>();


    public static SessionTracker getInstance() {
        return INSTANCE;
    }

    public void track(String cookie, Long id) {
        storage.put(cookie, id);
    }

    public void untrack(Long id) {
        for (String key : storage.keySet()) {
            if (storage.get(key).equals(id)) {
                storage.remove(key);
            }
        }
    }

    public void untrack(String cookie) {
        storage.remove(cookie);
    }

    public Long getIdByCookie(String cookie) {
        return storage.get(cookie);
    }

}
