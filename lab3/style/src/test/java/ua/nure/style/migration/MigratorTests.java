package ua.nure.style.migration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.nure.style.Migrator;
import ua.nure.style.dao.ConnectionFactory;
import ua.nure.style.dao.DAOFactory;
import ua.nure.style.dao.DAOType;
import ua.nure.style.dao.IMyDao;
import ua.nure.style.entity.*;

@SpringBootTest
class MigratorTests {

    Migrator migrator;
    PasswordEncoder passwordEncoder;

    private Service service;
    private User user;
    private Booking booking;
    private Role role;
    private Status status;

    IMyDao mongoDao;
    IMyDao mysqlDao;

    @Autowired
    public MigratorTests(
            Migrator migrator,
            PasswordEncoder passwordEncoder
    ) {
        this.migrator = migrator;
        this.passwordEncoder = passwordEncoder;
        mongoDao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MONGO);
        mysqlDao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MYSQL);
    }

    @Test
    @Disabled
    void migrateFromMongoToMysql() {
        migrator.migrate(
                mongoDao,
                mysqlDao
        );
    }

    @Test
    void migrateFromMysqlToMongo() {
        migrator.migrate(
                mysqlDao,
                mongoDao
        );
    }

}