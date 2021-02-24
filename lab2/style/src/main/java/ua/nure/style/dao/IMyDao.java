package ua.nure.style.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.nure.style.entity.*;

import java.util.List;
import java.util.Optional;

public interface IMyDao {
    Optional<Role> getRole(long id);
    Optional<Role> getRoleByName(String name);
    List<Role> getAllRoles();
    void saveRole(Role role);
    void updateRole(Role role, String[] params);
    void deleteRole(Role role);
    Optional<Booking> getBooking(long id);
    List<Booking> getAllBookings();
    void saveBooking(Booking booking);
    void addService(long equipmentId, long reservationId);
    void updateBooking(Booking booking, String[] params);
    void deleteBooking(Booking booking);
    List<Booking> getBookingsByEmail(String email);
    void updateStatus(Booking booking, Status status);
    void cancelBookingsFromUser(int userId);
    Optional<Service> getService(long id);
    List<Service> getAllServices();
    void saveService(Service service);
    void updateService(Service service, String[] params);
    void deleteService(Service service);
    List<Service> getServicesByBooking(long id);
    Optional<Status> getStatus(long id);
    Optional<Status> getStatusByName(String name);
    List<Status> getAllStatuses();
    void saveStatus(Status status);
    void updateStatus(Status status, String[] params);
    void deleteStatus(Status status);
    Optional<User> getUser(long id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    void saveUser(User user);
    void updateUser(User user, String[] params);
    void deleteUser(User user);
    UserDetails loadUserByUsername(String name);
}
