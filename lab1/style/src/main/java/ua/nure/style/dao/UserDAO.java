package ua.nure.style.dao;

import ua.nure.style.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class UserDAO implements IMyDao<User> {

    private Connection connection;
    private ResultSet resultSet;
    private RoleDAO roleDAO;

    @Autowired
    public UserDAO(ConnectionFactory connectionFactory,
                   RoleDAO roleDAO) {
        this.connection = connectionFactory.getConnection();
        this.roleDAO = roleDAO;
    }


    @Override
    public Optional<User> get(long id) {
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
                user.setRole(roleDAO.get(this.resultSet.getLong(6)).get());
                return Optional.of(user);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    public Optional<User> getByEmail(String email) {
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
                user.setRole(roleDAO.get(this.resultSet.getLong(6)).get());
                return Optional.of(user);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(22);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void save(User user) {
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
    public void update(User user, String[] params) {

    }

    @Override
    public void delete(User user) {

    }

    public UserDetails loadUserByUsername(String name) {
        User user = this.getByEmail(name).get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().toString().toUpperCase())
                .build();
    }
}
