package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class UserDaoJDBCImpl implements UserDao {

    Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }


    public void createUsersTable() {
        PreparedStatement preparedStatement = null;
        String sql = "CREATE TABLE  IF NOT EXISTS user" +
                     "(id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
                     "name VARCHAR (64)," +
                     "lastname VARCHAR (64)," +
                     "age TINYINT )";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            System.out.println ("Создана база user!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void dropUsersTable() {
        PreparedStatement preparedStatement = null;
        String sql = "DROP TABLE IF EXISTS user";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            System.out.println ("База удалена!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void saveUser(String name, String lastName, byte age)  {
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO user (name, lastName, age) VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("User с именем - " + name + " добавлен в базу");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        }
    }


    public void removeUserById(long id) {

        PreparedStatement preparedStatement = null;

        String sql = "DELETE FROM USER WHERE ID=?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        }
    }


    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }


    public void cleanUsersTable() {
        PreparedStatement preparedStatement = null;
        String sql = "TRUNCATE TABLE user";

        try {
            preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);

            preparedStatement.executeUpdate();
            System.out.println("База user очищена");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        }
    }
}
