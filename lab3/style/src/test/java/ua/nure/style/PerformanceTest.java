package ua.nure.style;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.nure.style.dao.ConnectionFactory;
import ua.nure.style.dao.DAOFactory;
import ua.nure.style.dao.DAOType;
import ua.nure.style.dao.IMyDao;
import ua.nure.style.entity.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class PerformanceTest {

    private final IMyDao dao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public PerformanceTest(
            PasswordEncoder passwordEncoder
    ) {
        this.passwordEncoder = passwordEncoder;
        this.dao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MYSQL);
    }

    @Test
    @Disabled
    void mongoInsertTest() {
        final Service service = new Service(
                "test name one",
                "test description one",
                "234.4"
        );

        List<Integer> amounts = Arrays.asList(100, 1000, 10_000, 50_000, 100_000);

        System.out.println("Mongo Inserts");
        for (Integer amount : amounts) {
            System.out.printf("%d inserts\n\n", amount);
            Instant before = Instant.now();
            for (int i = 0; i < amount; i++) {
                dao.saveService(service);
            }
            Instant after = Instant.now();
            System.out.println(Duration.between(before, after).getSeconds());
        }
    }

    @Test
    @Disabled
    void mysqlInsertTest() {
        final Service service = new Service(
                "test name one",
                "test description one",
                "324.1"
        );

        List<Integer> amounts = Arrays.asList(100, 1000, 10_000, 50_000, 100_000);

        System.out.println("Mysql Inserts");
        for (Integer amount : amounts) {
            System.out.printf("%d inserts\n\n", amount);
            Instant before = Instant.now();
            for (int i = 0; i < amount; i++) {
                dao.saveService(service);
            }
            Instant after = Instant.now();
            System.out.println(Duration.between(before, after).getSeconds());
        }

    }

    @Test
//    @Disabled
    void mongoReadTest() {
        final Service service = new Service(
                "test name one",
                "test description one",
                "234.1"
        );

        List<Integer> amounts = Arrays.asList(100, 1000, 5000);

        System.out.println("Mongo Reads");
        for (Integer amount : amounts) {
            System.out.printf("%d reads\n", amount);
            Instant before = Instant.now();
            for (int i = 0; i < amount; i++) {
                dao.getServiceByName(service.getName());
            }
            Instant after = Instant.now();
            System.out.println(Duration.between(before, after).getSeconds());
        }

    }

    @Test
    @Disabled
    void mysqlReadTest() {
        final Service service = new Service(
                "test name one",
                "test description one",
                "234.1"
        );

        List<Integer> amounts = Arrays.asList(1000);

        System.out.println("Mysql Reads");
        for (Integer amount : amounts) {
            System.out.printf("%d reads/n/n", amount);
            Instant before = Instant.now();
            for (int i = 0; i < amount; i++) {
                dao.getServiceByName(service.getName());
            }
            Instant after = Instant.now();
            System.out.println(Duration.between(before, after).getSeconds());
        }

    }

}