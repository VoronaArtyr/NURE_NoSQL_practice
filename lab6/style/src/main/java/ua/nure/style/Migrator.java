package ua.nure.style;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import ua.nure.style.dao.IMyDao;
import ua.nure.style.entity.*;

import java.util.ArrayList;
import java.util.List;

@Component
@Data @AllArgsConstructor
public class Migrator {

    public void migrate(IMyDao from, IMyDao to) {
        System.out.printf("Migrating from %s to %s%n", from, to);
        final var fromServiceList = from.getAllServices();
        System.out.println("Migrating Services");
        for (Service service : fromServiceList) {
            to.saveService(service);
        }
        System.out.printf("Migrated %d services%n", fromServiceList.size());

        final var fromRoleList = from.getAllRoles();
        System.out.println("Migrating Roles");
        for (Role role : fromRoleList) {
            to.saveRole(role);
        }
        System.out.printf("Migrated %d roles%n", fromRoleList.size());

        final var fromStatusList = from.getAllStatuses();
        System.out.println("Migrating Statues");
        for (Status status : fromStatusList) {
            to.saveStatus(status);
        }
        System.out.printf("Migrated %d statuses%n", fromStatusList.size());

        final var fromUserList = from.getAllUsers();
        System.out.println("Migrating Users");
        for (User user : fromUserList) {
            user.setRole(to.getRoleByName(user.getRole().getName()).get());
            to.saveUser(user);
        }
        System.out.printf("Migrated %d users%n", fromUserList.size());

        final var fromBookingList = from.getAllBookings();
        System.out.println("Migrating Bookings");
        for (Booking booking : fromBookingList) {
            List<Service> serviceList = new ArrayList<>();
            for (Service service : booking.getServices()) {
                service.setId(to.getServiceByName(service.getName()).get().getId());
                serviceList.add(service);
            }
            booking.setServices(serviceList);
        }
        for (Booking booking : fromBookingList) {
            booking.setClient(to.getUserByEmail(booking.getClient().getEmail()).get());
            booking.setStatus(to.getStatusByName(booking.getStatus().getName()).get());
            to.saveBooking(booking);
        }

        System.out.printf("Migrated %d bookings%n", fromBookingList.size());
    }

}
