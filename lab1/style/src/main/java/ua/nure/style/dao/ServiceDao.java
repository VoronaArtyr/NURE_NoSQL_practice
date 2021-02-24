package ua.nure.style.dao;

import ua.nure.style.entity.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ServiceDao implements IMyDao<Service> {

    private final Connection connection;
    private ResultSet resultSet;

    @Autowired
    public ServiceDao(ConnectionFactory connectionFactory) {
        this.connection = connectionFactory.getConnection();
    }

    @Override
    public Optional<Service> get(long id) {
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
    public List<Service> getAll() {
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
    public void save(Service service) {
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
    public void update(Service service, String[] params) {
        System.out.println("Not yet Implemented");
    }

    @Override
    public void delete(Service service) {
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

    public List<Service> getByReservation(long id) {
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
}
