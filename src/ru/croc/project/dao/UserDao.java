package ru.croc.project.dao;

import ru.croc.project.DataSource;
import ru.croc.project.models.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Получение всех пользователей из БД
     * @return все актуальные пользователи в БД
     */
    public ArrayList<User> getAllUsers() {

        ArrayList<User> users = new ArrayList<>();

        try {
            String sql = "select * from users";
            try (Statement statement = dataSource.getConnection().createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("ID");
                            String username = resultSet.getString("USER_NAME");
                            boolean isPromoted = resultSet.getBoolean("IS_PROMOTED");
                            users.add(new User(id, username, isPromoted));
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println("Ошибка при получении списка всех пользователей из БД");
        }

        return users;
    }

    /**
     * Поиск пользователя по имени
     * @param name имя пользователя
     * @return объект пользователя с актуальной информацией
     */
    public User findOne(String name) {
        int id = 0;
        boolean isPromoted = false;

        try {
            String sql = String.format("SELECT * FROM USERS u WHERE u.USER_NAME = '%s'", name);
            try (Statement statement = dataSource.getConnection().createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        while (resultSet.next()) {
                            id = resultSet.getInt("ID");
                            isPromoted = resultSet.getBoolean("IS_PROMOTED");
                        }
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (SQLException exception) {
            System.out.println("Ошибка при получении пользователя по имени из БД");
        }

        return new User(id, name, isPromoted);
    }

    /**
     * Метод сохранения пользователя в БД
     * @param user объект пользователя, передаваемый для сохранения
     */
    public void save(User user) {
        try {
            String sql = "insert into users (user_name, is_promoted) values (?, ?)";
            try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
                statement.setString(1, user.getName());
                statement.setBoolean(2, user.isPromoted());
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            System.out.println("Ошибка при сохранении пользователя");
        }
    }

}
