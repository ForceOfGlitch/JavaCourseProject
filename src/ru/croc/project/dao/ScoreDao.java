package ru.croc.project.dao;

import ru.croc.project.DataSource;
import ru.croc.project.models.Film;
import ru.croc.project.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ScoreDao {
    private final DataSource dataSource;

    public ScoreDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Метод сохранения оценки пользователя на фильм
     * Перезаписывает существующую, если такая имеется
     * @param user пользователь, совершающий оценку
     * @param film оцениваемый фильм
     * @param score непосредственная оценка
     */
    public void saveScore(User user, Film film, int score){
        try {

            String sql = String.format("SELECT * FROM SCORES WHERE FILM_ID = '%d' AND USER_ID = '%d'", film.getId(), new UserDao(DataSource.getDataSource()).findOne(user.getName()).getId());

            try (Statement statement = dataSource.getConnection().createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        if (resultSet.next()) {
                            sql = "UPDATE SCORES SET SCORE  = ? WHERE FILM_ID = ? AND USER_ID = ?";
                            try (PreparedStatement anotherStatement = dataSource.getConnection().prepareStatement(sql)) {
                                anotherStatement.setInt(1, score);
                                anotherStatement.setInt(2, film.getId());
                                anotherStatement.setInt(3, user.getId());
                                anotherStatement.executeUpdate();
                            }
                        } else {
                            sql = "insert into scores (USER_ID, FILM_ID, SCORE) values (?, ?, ?)";
                            try (PreparedStatement anotherStatement = dataSource.getConnection().prepareStatement(sql)) {
                                anotherStatement.setInt(1, user.getId());
                                anotherStatement.setInt(2, film.getId());
                                anotherStatement.setInt(3, score);
                                anotherStatement.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении оценки");
        }

        System.out.println("Оценка сохранена");
    }
}
