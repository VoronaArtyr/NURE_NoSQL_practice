package ua.nure.style.dao;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import ua.nure.style.dao.ConnectionFactory;
import ua.nure.style.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ToString
@EqualsAndHashCode
public class MysqlDAO implements IMyDao {

    private final Connection connection;

    public MysqlDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }

    @Override
    public Optional<Service> getService(String id) {
        String sql = "SELECT service_id, name, description FROM service WHERE service_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Service service = new Service();
                service.setId(resultSet.getString(1));
                service.setName(resultSet.getString(2));
                service.setDescription(resultSet.getString(3));
                return Optional.of(service);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> getRole(String id) {
        String sql = "SELECT role_id, name FROM role WHERE role_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getString(1));
                role.setName(resultSet.getString(2));
                return Optional.of(role);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        String sql = "SELECT role_id, name FROM role WHERE name = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getString(1));
                role.setName(resultSet.getString(2));
                return Optional.of(role);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Booking> getBooking(String id) {
        String sql = "SELECT booking_id, fk_status, fk_client, starts_at, ends_at FROM booking WHERE booking_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getString(1));
                booking.setStatus(getStatus(resultSet.getString(2)).get());
                booking.setClient( getUser(resultSet.getString(3)).get() );
                booking.setStartsAt(resultSet.getDate(4));
                booking.setEndsAt(resultSet.getDate(5));
                booking.setServices(this.getServicesByBooking(booking.getId()));
                return Optional.of(booking);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUser(String id) {
        String sql = "SELECT user_id, fname, lname, email, password, role_id FROM user WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString(1));
                user.setFname(resultSet.getString(2));
                user.setLname(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
                user.setRole(getRole(resultSet.getString(6)).get());
                return Optional.of(user);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Status> getStatus(String id) {
        String sql = "SELECT status_id, name FROM status WHERE status_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Status status = new Status();
                status.setId(resultSet.getString(1));
                status.setName(resultSet.getString(2));
                return Optional.of(status);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Status> getStatusByName(String name) {
        String sql = "SELECT status_id, name FROM status WHERE name = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Status status = new Status();
                status.setId(resultSet.getString(1));
                status.setName(resultSet.getString(2));
                return Optional.of(status);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public List<Service> getAllServices() {
        String sql = "SELECT service_id, name, description FROM service";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Service> serviceList = new ArrayList<>();
            while (resultSet.next()) {
                Service service = new Service();
                service.setId(resultSet.getString(1));
                service.setName(resultSet.getString(2));
                service.setDescription(resultSet.getString(3));
                serviceList.add(service);
            }
            return serviceList;
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Role> getAllRoles() {
        String sql = "SELECT role_id, name FROM role";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getString(1));
                role.setName(resultSet.getString(2));
                roles.add(role);
            }
            return roles;
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Booking> getAllBookings() {
        String sql = "SELECT booking_id, fk_status, fk_client, starts_at, ends_at FROM booking";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getString(1));
                booking.setStatus(getStatus(resultSet.getString(2)).get());
                booking.setClient(getUser(resultSet.getString(3)).get());
                booking.setStartsAt(resultSet.getDate(4));
                booking.setEndsAt(resultSet.getDate(5));
                booking.setServices(getServicesByBooking(booking.getId()));
                bookings.add(booking);
            }
            return bookings;
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        String sql = "SELECT user_id, fname, lname, email, password, role_id FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString(1));
                user.setFname(resultSet.getString(2));
                user.setLname(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
                user.setRole(getRole(resultSet.getString(6)).get());
                return Optional.of(user);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT user_id, fname, lname, email, password, role_id FROM user";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString(1));
                user.setFname(resultSet.getString(2));
                user.setLname(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
                user.setRole(getRole(resultSet.getString(6)).get());
                users.add(user);
            }
            return users;
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Status> getAllStatuses() {
        String sql = "SELECT status_id, name FROM status";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Status> statuses = new ArrayList<>();
            while (resultSet.next()) {
                Status status = new Status();
                status.setId(resultSet.getString(1));
                status.setName(resultSet.getString(2));
                statuses.add(status);
            }
            return statuses;
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Collections.emptyList();
    }

    @Override
    public void saveService(Service service) {
        String sql = "INSERT INTO service (name, description) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, service.getName());
            preparedStatement.setString(2, service.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(3);
        }
    }

    @Override
    public void saveRole(Role role) {
        String sql = "INSERT INTO role(name) VALUES (?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
    }

    @Override
    public void saveBooking(Booking booking) {
        String sql = "INSERT INTO booking (fk_status, fk_client, starts_at, ends_at) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, booking.getStatus().getId());
            preparedStatement.setString(2, booking.getClient().getId());
            preparedStatement.setDate(3, booking.getStartsAt());
            preparedStatement.setDate(4, booking.getEndsAt());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                for (Service service : booking.getServices()) {
                    this.addService(service.getId(), resultSet.getString(1));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
    }

    @Override
    public void saveUser(User user) {
        String sql = "INSERT INTO user (fname, lname, email, password, role_id) VALUES (?, ?, ?, ?, ?) ";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFname());
            preparedStatement.setString(2, user.getLname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
    }

    @Override
    public void saveStatus(Status status) {
        String sql = "INSERT INTO status(name) VALUES (?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, status.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
    }

    @Override
    public void deleteRole(Role role) {
        System.err.println("Not yet implemented");
    }

    @Override
    public void deleteBooking(Booking booking) {
        String sql = "DELETE FROM booking WHERE booking_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, booking.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(234);
        }
    }


    @Override
    public List<Service> getServicesByBooking(String id) {
        String sql = "select service_id, name, description from service_booked inner join service on service.service_id = service_booked.fk_service where fk_booking = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Service> serviceList = new ArrayList<>();
            while (resultSet.next()) {
                serviceList.add(
                        new Service(resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3)
                        )
                );
            }
            return serviceList;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();

    }

    @Override
    public List<Booking> getBookingsByEmail(String email) {
        String sql = "SELECT booking_id, fk_status, fk_client, starts_at, ends_at FROM booking WHERE fk_client = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, getUserByEmail(email).get().getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setClient(getUserByEmail(email).get());
                booking.setId(resultSet.getString(1));
                booking.setStatus(getStatus(resultSet.getString(2)).get());
                booking.setStartsAt(resultSet.getDate(4));
                booking.setEndsAt(resultSet.getDate(5));
                booking.setServices(getServicesByBooking(booking.getId()));
                bookings.add(booking);
            }
            return bookings;
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
        return Collections.emptyList();
    }

    @Override
    public void deleteUser(User user) {
        System.err.println("Not yet implemented");
    }

    @Override
    public void deleteStatus(Status status) {
        System.err.println("Not yet implemented");
    }

    @Override
    public void updateStatus(Booking booking, Status status) {
        String sql = "UPDATE booking SET fk_status = ? WHERE booking_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, status.getId());
            preparedStatement.setString(2, booking.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(234);
        }

    }

    @Override
    public void addService(String serviceId, String bookingId) {
        String sql = "INSERT INTO service_booked VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, bookingId);
            preparedStatement.setString(2, serviceId);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = getUserByEmail(name).get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().toString().toUpperCase())
                .build();
    }

    @Override
    public Optional<Service> getServiceByName(String name) {
        String sql = "SELECT service_id, name, description FROM service WHERE name = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Service service = new Service();
                service.setId(resultSet.getString(1));
                service.setName(resultSet.getString(2));
                service.setDescription(resultSet.getString(3));
                return Optional.of(service);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
        return Optional.empty();
    }
}
