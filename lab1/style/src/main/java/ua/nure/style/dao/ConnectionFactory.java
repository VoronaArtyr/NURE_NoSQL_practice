package ua.nure.style.dao;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionFactory {
    private static final String USER = "root";
    private static final String URL = "jdbc:mysql://localhost:3306/style";
    private static final String PASS = "password";

    private static Connection connection;

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASS);
                return connection;
            } catch (SQLException exception) {
                exception.printStackTrace();
                System.exit(1);
                return null;
            }
        } else {
            return connection;
        }
    }
}
