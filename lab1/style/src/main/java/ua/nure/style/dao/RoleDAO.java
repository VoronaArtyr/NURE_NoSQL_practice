package ua.nure.style.dao;

import ua.nure.style.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class RoleDAO implements IMyDao<Role> {

    private Connection connection;
    private ResultSet resultSet;

    @Autowired
    public RoleDAO(ConnectionFactory connectionFactory) {
        this.connection = connectionFactory.getConnection();
    }

    @Override
    public Optional<Role> get(long id) {
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


    public Optional<Role> getByName(String name) {
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
    public List<Role> getAll() {
        System.out.println("Not yet implemented");
        return null;
    }

    @Override
    public void save(Role role) {
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
    public void update(Role role, String[] params) {
        System.out.println("Not yet implemented");
    }

    @Override
    public void delete(Role role) {
        System.out.println("Not yet implemented");
    }
}
