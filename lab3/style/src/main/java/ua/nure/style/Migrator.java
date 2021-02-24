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
        assert from != null;
        assert to != null;
        System.out.println(from);
        System.out.println(to);
        final var fromServiceList = from.getAllServices();
        for (Service service : fromServiceList) {
            to.saveService(service);
        }

        final var fromRoleList = from.getAllRoles();
        for (Role role : fromRoleList) {
            to.saveRole(role);
        }

        final var fromStatusList = from.getAllStatuses();
        for (Status status : fromStatusList) {
            to.saveStatus(status);
        }

        final var fromUserList = from.getAllUsers();
        for (User user : fromUserList) {
            user.setRole(to.getRoleByName(user.getRole().getName()).get());
            to.saveUser(user);
        }

        final var fromBookingList = from.getAllBookings();
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
    }

}
