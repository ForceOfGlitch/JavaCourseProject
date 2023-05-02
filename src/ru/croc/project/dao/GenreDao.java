package ru.croc.project.dao;

import ru.croc.project.DataSource;
import ru.croc.project.models.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GenreDao {
    private final DataSource dataSource;

    public GenreDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Сохранение жанра
     * @param genre название жанра
     */
    public void save(String genre){
        try {
            String sql = "insert into genres (genre_name) values (?)";
            try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
                statement.setString(1, genre);
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            System.out.println("Ошибка при сохранении жанра");
        }
    }

    /**
     * проверка существования жанра
     * @param genre название жанра
     * @return
     */
    public boolean isGenreExists(String genre){
        try{
            String sql = String.format("SELECT *\n" +
                    "FROM GENRES g \n" +
                    "WHERE g.GENRE_NAME = '%s'", genre);
            try (Statement statement = dataSource.getConnection().createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        if (resultSet.next()) {
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException exception){
            System.out.println("Ошибка при поиске жанра в БД");
        }

        return false;
    }

    /**
     * Получение всех жанров из БД
     * @return актуальный список всех жанров
     */
    public ArrayList<Genre> getAllGenres(){
        ArrayList<Genre> genres = new ArrayList<>();

        try {
            String sql = "select * from genres";
            try (Statement statement = dataSource.getConnection().createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        while (resultSet.next()) {
                            String genreName = resultSet.getString("GENRE_NAME");
                            Genre genre = new Genre();
                            genre.setName(genreName);
                            genres.add(genre);
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println("Ошибка при получении списка всех жанров из БД");
        }

        return genres;
    }
}
