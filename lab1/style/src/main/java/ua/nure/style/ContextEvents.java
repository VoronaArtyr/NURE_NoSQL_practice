package ua.nure.style;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.nure.style.dao.*;
import ua.nure.style.entity.Role;
import ua.nure.style.entity.Status;
import ua.nure.style.entity.User;

@Component
public class ContextEvents {

    final
    StatusDAO statusDAO;
    final
    RoleDAO roleDAO;
    final
    UserDAO userDAO;
    final
    BookingDao bookingDao;
    final
    ServiceDao serviceDao;
    final
    PasswordEncoder passwordEncoder;

    public ContextEvents(StatusDAO statusDAO,
                         RoleDAO roleDAO,
                         UserDAO userDAO,
                         BookingDao bookingDao,
                         ServiceDao serviceDao,
                         PasswordEncoder passwordEncoder
    ) {
        this.statusDAO = statusDAO;
        this.roleDAO = roleDAO;
        this.userDAO = userDAO;
        this.bookingDao = bookingDao;
        this.serviceDao = serviceDao;
        this.passwordEncoder = passwordEncoder;
    }

//    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent event) {
        statusDAO.save(new Status("Unprocessed"));
        statusDAO.save(new Status("Approved"));
        statusDAO.save(new Status("Cancelled"));
        statusDAO.save(new Status("Done"));

        roleDAO.save(new Role("client"));
        roleDAO.save(new Role("admin"));

        User defaultUser = new User();
        defaultUser.setFname("Default");
        defaultUser.setLname("User");
        defaultUser.setEmail("default.user@example.com");
        defaultUser.setPassword(passwordEncoder.encode("password"));
        defaultUser.setRole(roleDAO.getByName("client").get());
        userDAO.save(defaultUser);

        User defaultAdmin = new User();
        defaultUser.setFname("Default");
        defaultUser.setLname("Admin");
        defaultUser.setEmail("default.admin@example.com");
        defaultUser.setPassword(passwordEncoder.encode("password"));
        defaultUser.setRole(roleDAO.getByName("admin").get());
        userDAO.save(defaultUser);

//        equipmentDAO.save(new Service("Microphone", "Record voice"));
//        equipmentDAO.save(new Service("Guitar", "String-based"));
//        equipmentDAO.save(new Service("Violin", "idk"));
    }
}
