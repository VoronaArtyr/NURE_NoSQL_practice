package ua.nure.style.dao;

import ua.nure.style.entity.Status;
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
public class StatusDAO implements IMyDao<Status> {

    private ResultSet resultSet;
    private Connection connection;

    @Autowired
    public StatusDAO(ConnectionFactory connectionFactory) {
        this.connection = connectionFactory.getConnection();
    }


    @Override
    public Optional<Status> get(long id) {
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

    public Optional<Status> getByName(String name) {
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
    public List<Status> getAll() {
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
    public void save(Status status) {
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
    public void update(Status status, String[] params) {

    }

    @Override
    public void delete(Status status) {

    }
}
