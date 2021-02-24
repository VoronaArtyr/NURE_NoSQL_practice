package ua.nure.style.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.nure.style.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MysqlDAO implements IMyDao {

    private final Connection connection;
    private ResultSet resultSet;


    public MysqlDAO(ConnectionFactory connectionFactory) {
        this.connection = new ConnectionFactory().getConnection();
    }


    @Override
    public Optional<Role> getRole(long id) {
        String sql = "SELECT role_id, name FROM role WHERE role_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                Role role = new Role();
                role.setId(this.resultSet.getLong(1));
                role.setName(this.resultSet.getString(2));
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
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                Role role = new Role();
                role.setId(this.resultSet.getLong(1));
                role.setName(this.resultSet.getString(2));
                return Optional.of(role);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public List<Role> getAllRoles() {
        System.out.println("Not yet implemented");
        return null;
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
    public void updateRole(Role role, String[] params) {
        System.out.println("Not yet implemented");
    }

    @Override
    public void deleteRole(Role role) {
        System.out.println("not implemented here");
    }

    @Override
    public Optional<Booking> getBooking(long id) {
        String sql = "SELECT booking_id, fk_status, fk_client, starts_at, ends_at from booking where booking_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(this.resultSet.getLong(1));
                booking.setStatus(this.getStatus(this.resultSet.getLong(2)).get());
                booking.setClient(this.getUser(this.resultSet.getLong(3)).get());
                booking.setStartsAt(this.resultSet.getDate(4));
                booking.setEndsAt(this.resultSet.getDate(5));
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
    public List<Booking> getAllBookings() {
        String sql  = "select booking_id, fk_status, fk_client, starts_at, ends_at from booking";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            this.resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while (this.resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(this.resultSet.getLong(1));
                booking.setStatus(this.getStatus(this.resultSet.getLong(2)).get());
                booking.setClient(this.getUser(this.resultSet.getLong(3)).get());
                booking.setStartsAt(this.resultSet.getDate(4));
                booking.setEndsAt(this.resultSet.getDate(5));
                booking.setServices(this.getServicesByBooking(booking.getId()));
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
    public void saveBooking(Booking booking) {
        String sql = "insert into booking(fk_status, fk_client, starts_at, ends_at) value (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, booking.getStatus().getId());
            preparedStatement.setLong(2, booking.getClient().getId());
            preparedStatement.setDate(3, (Date) booking.getStartsAt());
            preparedStatement.setDate(4, (Date) booking.getEndsAt());
            preparedStatement.executeUpdate();
            this.resultSet = preparedStatement.getGeneratedKeys();
            while (this.resultSet.next()) {
                for (Service service : booking.getServices()) {
                    this.addService(service.getId(), this.resultSet.getLong(1));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
    }

    @Override
    public void addService(long equipmentId, long reservationId) {
        String sql = "INSERT INTO service_booked VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, reservationId);
            preparedStatement.setLong(2, equipmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void updateBooking(Booking booking, String[] params) {

    }

    @Override
    public void deleteBooking(Booking booking) {
        String sql = "DELETE FROM booking WHERE booking_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, booking.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(234);
        }

    }

    @Override
    public List<Booking> getBookingsByEmail(String email) {
        return Collections.emptyList();
    }

    @Override
    public void updateStatus(Booking booking, Status status) {
        String sql = "UPDATE booking SET fk_status = ? WHERE booking_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, status.getId());
            preparedStatement.setLong(2, booking.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(234);
        }

    }

    @Override
    public void cancelBookingsFromUser(int userId) {
        try {
            CallableStatement callableStatement = this.connection.prepareCall("{call closeBookingsFromUser(?)}");
            callableStatement.setInt(1, userId);
            callableStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Optional<Service> getService(long id) {
        String sql = "SELECT service_id, name, description, price FROM service WHERE service_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                Service service = new Service();
                service.setId(this.resultSet.getLong(1));
                service.setName(this.resultSet.getString(2));
                service.setDescription(this.resultSet.getString(3));
                service.setPrice(this.resultSet.getFloat(4));
                return Optional.of(service);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return Optional.empty();
    }

    @Override
    public List<Service> getAllServices() {
        String sql = "SELECT service_id, name, description, price FROM service";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            this.resultSet = preparedStatement.executeQuery();
            List<Service> serviceList = new ArrayList<>();
            while (this.resultSet.next()) {
                Service service = new Service();
                service.setId(this.resultSet.getLong(1));
                service.setName(this.resultSet.getString(2));
                service.setDescription(this.resultSet.getString(3));
                service.setPrice(this.resultSet.getFloat(4));
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
    public void saveService(Service service) {
        String sql = "INSERT INTO service (name, description, price) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, service.getName());
            preparedStatement.setString(2, service.getDescription());
            preparedStatement.setFloat(3, service.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void updateService(Service service, String[] params) {
        System.out.println("Not yet Implemented");
    }

    @Override
    public void deleteService(Service service) {
        String sql = "DELETE FROM service WHERE service_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, service.getId());
            preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(4);
        }
    }

    @Override
    public List<Service> getServicesByBooking(long id) {
        String sql = "SELECT service_booked.fk_service, name, description, price from service_booked inner join service s on service_booked.fk_service = s.service_id where fk_booking = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();
            List<Service> serviceList = new ArrayList<>();
            while (this.resultSet.next()) {
                serviceList.add(
                        new Service(this.resultSet.getLong(1),
                                this.resultSet.getString(2),
                                this.resultSet.getString(3),
                                this.resultSet.getFloat(4)
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
    public Optional<Status> getStatus(long id) {
        String sql = "SELECT status_id, name FROM status WHERE status_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                Status status = new Status();
                status.setId(this.resultSet.getLong(1));
                status.setName(this.resultSet.getString(2));
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
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                Status status = new Status();
                status.setId(this.resultSet.getLong(1));
                status.setName(this.resultSet.getString(2));
                return Optional.of(status);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public List<Status> getAllStatuses() {
        String sql = "SELECT status_id, name FROM status";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            this.resultSet = preparedStatement.executeQuery();
            List<Status> statuses = new ArrayList<>();
            while (this.resultSet.next()) {
                Status status = new Status();
                status.setId(this.resultSet.getLong(1));
                status.setName(this.resultSet.getString(2));
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
    public void updateStatus(Status status, String[] params) {

    }

    @Override
    public void deleteStatus(Status status) {

    }

    @Override
    public Optional<User> getUser(long id) {
        String sql = "SELECT user_id, fname, lname, email, password, role_id FROM user WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                User user = new User();
                user.setId(this.resultSet.getLong(1));
                user.setFname(this.resultSet.getString(2));
                user.setLname(this.resultSet.getString(3));
                user.setEmail(this.resultSet.getString(4));
                user.setPassword(this.resultSet.getString(5));
                user.setRole(this.getRole(this.resultSet.getLong(6)).get());
                return Optional.of(user);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        String sql = "SELECT user_id, fname, lname, email, password, role_id FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                User user = new User();
                user.setId(this.resultSet.getLong(1));
                user.setFname(this.resultSet.getString(2));
                user.setLname(this.resultSet.getString(3));
                user.setEmail(this.resultSet.getString(4));
                user.setPassword(this.resultSet.getString(5));
                user.setRole(this.getRole(this.resultSet.getLong(6)).get());
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
        return null;
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
            preparedStatement.setLong(5, user.getRole().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
    }

    @Override
    public void updateUser(User user, String[] params) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = this.getUserByEmail(name).get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().toString().toUpperCase())
                .build();
    }

}