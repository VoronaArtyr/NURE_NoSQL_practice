package ua.nure.style;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StyleApplication {

    public final static String COOKIE_NAME = "session_id";

    public static void main(String[] args) {
        SpringApplication.run(StyleApplication.class, args);
    }


}
