package ru.croc.project.dao;

import ru.croc.project.DataSource;
import ru.croc.project.models.Film;
import ru.croc.project.models.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FilmDao {
    private final DataSource dataSource;

    public FilmDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Поиск фильма по названию, выбрасывает ошибку, если такого фильма нет
     * @param filmName название
     * @return искомый фильм
     */
    public Film findOne(String filmName) {
        List<String> genres = new ArrayList<>();
        int id = 0;

        try {
            String sql = String.format("SELECT f.ID, FILM_NAME, GENRE_NAME \n" +
                    "FROM FILMS f\n" +
                    "LEFT JOIN films_genres ON f.id = films_genres.film_id\n" +
                    "LEFT JOIN genres ON films_genres.genre_id = genres.id\n" +
                    "WHERE f.FILM_NAME = '%s'", filmName);
            try (Statement statement = dataSource.getConnection().createStatement()) {

                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        boolean isEmpty = true;
                        while (resultSet.next()) {
                            isEmpty = false;
                            String genre = resultSet.getString("GENRE_NAME");
                            if (genre != null && !genre.equals("")) genres.add(genre);
                            id = resultSet.getInt("ID");
                        }
                        if (isEmpty){
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println("Ошибка при получении фильма по имени из БД");
        }

        return new Film(id, filmName, genres);
    }

    /**
     * Метод формирования и печати топа фильмов в определённом жанре (опционально)
     * @param filmsCount топ сколько требуется
     * @param genre жанр
     */
    public void printTop(int filmsCount, String genre){
        try{
            String sql;

            if (genre.equals("")){
                sql = String.format("SELECT films.FILM_NAME, AVG(scores.score) AS average_score\n" +
                        "FROM films\n" +
                        "JOIN films_genres ON films.id = films_genres.film_id\n" +
                        "JOIN genres ON films_genres.genre_id = genres.id\n" +
                        "JOIN scores ON films.id = scores.film_id\n" +
                        "GROUP BY films.id\n" +
                        "ORDER BY average_score DESC");
            } else {
                sql = String.format("SELECT films.FILM_NAME, AVG(scores.score) AS average_score\n" +
                        "FROM films\n" +
                        "JOIN films_genres ON films.id = films_genres.film_id\n" +
                        "JOIN genres ON films_genres.genre_id = genres.id\n" +
                        "JOIN scores ON films.id = scores.film_id\n" +
                        "WHERE genres.genre_name = '%s'\n" +
                        "GROUP BY films.id\n" +
                        "ORDER BY average_score DESC", genre);
            }
            try (Statement statement = dataSource.getConnection().createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        int i = 0;
                        while (resultSet.next()) {
                            if (i >= filmsCount) break;

                            String name = resultSet.getString("FILM_NAME");
                            float score = resultSet.getFloat("AVERAGE_SCORE");

                            i++;
                            System.out.println(String.format("%d. Фильм: '%s' Рейтинг: '%f'", i, name, score));
                        }

                        if(i < filmsCount - 1){
                            System.out.println("В базе нет столько фильмов требуемого жанра с ненулевыми оценками, показан максимум");
                        }
                    }
                }
            }
        } catch (SQLException exception){
            System.out.println("Ошибка при формировании топа");
        }
    }

    /**
     * Сохранение фильма в БД
     * @param film
     */
    public void save(Film film) {
        try {
            String sql = "insert into films (film_name) values (?)";
            try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
                statement.setString(1, film.getName());
                statement.executeUpdate();
            }

            Film createdFilm = findOne(film.getName());

            for (String genre :
                    film.getGenres()) {
                sql = String.format("SELECT * FROM GENRES WHERE GENRE_NAME = '%s'", genre);
                try (Statement statement = dataSource.getConnection().createStatement()) {
                    boolean hasResult = statement.execute(sql);
                    if (hasResult) {
                        try (ResultSet resultSet = statement.getResultSet()) {
                            if (resultSet.next()) {
                                sql = "insert into films_genres (film_id, genre_id) VALUES (?, ?)";
                                try (PreparedStatement anotherStatement = dataSource.getConnection().prepareStatement(sql)) {
                                    anotherStatement.setInt(1, createdFilm.getId());
                                    anotherStatement.setInt(2, resultSet.getInt("ID"));
                                    anotherStatement.executeUpdate();
                                }
                            }
                        }
                    } else {
                        System.out.println(String.format("Не найден жанр %s. Невозможно добавить в базу зависимость.", genre));
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println("Ошибка при сохранении фильма");
        }
    }

    /**
     * Удаление фильма с учётом его связей в остальных таблицах
     * @param film
     */
    public void delete(Film film) {
        try {

            String sql = "delete from films_genres where film_id = ?";
            try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
                statement.setInt(1, film.getId());
                statement.executeUpdate();
            }

            sql = "delete from scores where film_id = ?";
            try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
                statement.setInt(1, film.getId());
                statement.executeUpdate();
            }

            sql = "delete from films where id = ?";
            try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
                statement.setInt(1, film.getId());
                statement.executeUpdate();
            }

        } catch (SQLException exception){
            System.out.println("Ошибка при удалении фильма");
        }
    }

    /**
     * Метод возвращает список объектов фильмов с их именами внутри
     * @return
     */
    public ArrayList<Film> getAllFilmsNames(){
        ArrayList<Film> films = new ArrayList<>();

        try {
            String sql = "select * from films";
            try (Statement statement = dataSource.getConnection().createStatement()) {
                boolean hasResult = statement.execute(sql);
                if (hasResult) {
                    try (ResultSet resultSet = statement.getResultSet()) {
                        while (resultSet.next()) {
                            String genreName = resultSet.getString("FILM_NAME");
                            Film film = new Film();
                            film.setName(genreName);
                            films.add(film);
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println("Ошибка при получении списка названий всех фильмов из БД");
        }

        return films;
    }
}
