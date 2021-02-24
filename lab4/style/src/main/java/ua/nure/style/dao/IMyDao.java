package ua.nure.style.dao;

import org.springframework.security.core.userdetails.UserDetails;
import ua.nure.style.entity.*;

import java.util.List;
import java.util.Optional;

public interface IMyDao {

    Optional<Service> getService(String id);
    Optional<Service> getServiceByName(String name);
    List<Service> getAllServices();
    List<Service> getServicesByBooking(String id);
    void saveService(Service equipment);
    void addService(String equipmentId, String reservationId);

    Optional<Role> getRole(String id);
    Optional<Role> getRoleByName(String name);
    void deleteRole(Role role);
    void saveRole(Role role);
    List<Role> getAllRoles();

    Optional<Booking> getBooking(String id);
    List<Booking> getAllBookings();
    void saveBooking(Booking reservation);
    void deleteBooking(Booking reservation);
    List<Booking> getBookingsByEmail(String email);
    void updateStatus(Booking reservation, Status status);

    Optional<User> getUser(String id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    void saveUser(User user);
    void deleteUser(User user);
    UserDetails loadUserByUsername(String name);

    Optional<Status> getStatus(String id);
    void saveStatus(Status status);
    Optional<Status> getStatusByName(String name);
    List<Status> getAllStatuses();
    void deleteStatus(Status status);
}
