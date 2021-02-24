package ua.nure.style.dao;

import ua.nure.style.entity.Booking;
import ua.nure.style.entity.Service;
import ua.nure.style.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class BookingDao implements IMyDao<Booking> {

    private final Connection connection;
    private ResultSet resultSet;
    private final UserDAO userDAO;
    private final StatusDAO statusDAO;
    private final ServiceDao serviceDao;
    @Autowired
    public BookingDao(ConnectionFactory connectionFactory, UserDAO userDAO, StatusDAO statusDAO, ServiceDao serviceDao) {
        this.connection = connectionFactory.getConnection();
        this.userDAO = userDAO;
        this.statusDAO = statusDAO;
        this.serviceDao = serviceDao;
    }

    @Override
    public Optional<Booking> get(long id) {
        String sql = "SELECT booking_id, fk_status, fk_client, starts_at, ends_at from booking where booking_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(this.resultSet.getLong(1));
                booking.setStatus(statusDAO.get(this.resultSet.getLong(2)).get());
                booking.setClient(userDAO.get(this.resultSet.getLong(3)).get());
                booking.setStartsAt(this.resultSet.getDate(4));
                booking.setEndsAt(this.resultSet.getDate(5));
                booking.setServices(this.serviceDao.getByReservation(booking.getId()));
                return Optional.of(booking);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(2);
        }
        return Optional.empty();
    }

    @Override
    public List<Booking> getAll() {
        String sql  = "select booking_id, fk_status, fk_client, starts_at, ends_at from booking";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            this.resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while (this.resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(this.resultSet.getLong(1));
                booking.setStatus(statusDAO.get(this.resultSet.getLong(2)).get());
                booking.setClient(userDAO.get(this.resultSet.getLong(3)).get());
                booking.setStartsAt(this.resultSet.getDate(4));
                booking.setEndsAt(this.resultSet.getDate(5));
                booking.setServices(this.serviceDao.getByReservation(booking.getId()));
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
    public void save(Booking booking) {
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

    private void addService(long equipmentId, long reservationId) {
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
    public void update(Booking booking, String[] params) {

    }

    @Override
    public void delete(Booking booking) {
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

    public List<Booking> getByEmail(String email) {
        return Collections.emptyList();
    }

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

    public void cancelBookingsFromUser(int userId) {
        try {
            CallableStatement callableStatement = this.connection.prepareCall("{call closeBookingsFromUser(?)}");
            callableStatement.setInt(1, userId);
            callableStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}