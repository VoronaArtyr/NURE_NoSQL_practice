package ua.nure.style;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ua.nure.style.dao.*;
import ua.nure.style.entity.*;

import java.sql.Date;
import java.util.List;

//@Component
public class ContextEvents {

//    @EventListener
    public void initDummyData(ContextRefreshedEvent event) {
        System.out.println(event);
        MongoDBDAO dao = new MongoDBDAO();
        dao.saveStatus(new Status("Unprocessed"));
        dao.saveStatus(new Status("Approved"));
        dao.saveStatus(new Status("Cancelled"));
        dao.saveStatus(new Status("Done"));

        dao.saveRole(new Role("client"));
        dao.saveRole(new Role("admin"));

        for (int i = 0; i < 1000; i++) {
            User defaultUser = new User();
            defaultUser.setFname("Default");
            defaultUser.setLname("User");
            defaultUser.setEmail("default.user@example.com");
            defaultUser.setPassword("password");
            defaultUser.setRole(dao.getRoleByName("client").get());
            dao.saveUser(defaultUser);

            User defaultAdmin = new User();
            defaultAdmin.setFname("Default");
            defaultAdmin.setLname("Admin");
            defaultAdmin.setEmail("default.admin@example.com");
            defaultAdmin.setPassword("password");
            defaultAdmin.setRole(dao.getRoleByName("admin").get());
            System.out.println(dao.getRoleByName("admin"));
            dao.saveUser(defaultAdmin);
        }


        dao.saveService(new Service("ПРОФЕССИОНАЛЬНАЯ МУЖСКАЯ СТРИЖКА", "", "21.1"));
        dao.saveService(new Service("ПРОФЕССИОНАЛЬНАЯ СТРИЖКА + СТРИЖКА БОРОДЫ", "", "324"));
        dao.saveService(new Service("СТРИЖКА МАШИНКОЙ", "", "324"));
        dao.saveService(new Service("КОРОЛЕВСКОЕ БРИТЬЕ", "", "324"));


        List<Service> serviceList = dao.getAllServices();
        for (int i = 0; i < 10000; i++) {
            Booking booking = new Booking();
            booking.setServices(serviceList);
            booking.setClient(dao.getUserByEmail("default.user@example.com").get());
            booking.setStatus(dao.getStatusByName("Done").get());
            booking.setStartsAt(new Date(System.currentTimeMillis()));
            booking.setEndsAt(new Date(System.currentTimeMillis()));
            dao.saveBooking(booking);
        }
    }
}
