package ru.croc.project;

import ru.croc.project.models.Film;
import ru.croc.project.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataSource {
    private static DataSource dataSource;
    private final List<User> users;
    private final List<Film> films;

    private DataSource(){
        users = new ArrayList<>();
        users.add(new User("user0", true));
        users.add(new User("user1", false));
        users.add(new User("user2", false));
        users.add(new User("user3", false));

        films = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        genres.add("g1");
        genres.add("g2");
        films.add(new Film("n1", genres));
        genres = new ArrayList<>();
        genres.add("g2");
        films.add(new Film("n2", genres));
        genres = new ArrayList<>();
        genres.add("g1");
        genres.add("g3");
        films.add(new Film("n3", genres));
    }

    /**
     * Получение источника данных
     * @return новый источник данных, если ещё не был создан, либо ссылку на существующий
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new DataSource();
            return dataSource;
        }
        return dataSource;
    }

    public ArrayList<User> getUsers() {
        // TODO: Получение пользователей из БД
        return new ArrayList<>(users);
    }

    public Optional<User> findUserByName(String name){
        // TODO: Поиск пользователя в БД
        for (User user : users) {
            if (user.getName().equals(name)) return Optional.of(user);
        }
        return Optional.empty();
    }

    public void saveUser(User user){
        // TODO: Сохранение пользователя в БД
        users.add(user);
    }

    public void deleteUser(User user){
        // TODO: Удаление пользователя из БД
        users.remove(user);
    }

    public void saveFilm(Film film){
        // TODO: Сохранение пользователя в БД
        films.add(film);
    }

    public void deleteFilm(Film film){
        // TODO: Удаление фильма из БД
        films.remove(film);
    }

    public List<Film> getTop(int filmsCount, String genre){
        // TODO: Получение из БД топ {filmsCount} фильмов в жанре {genre}
        return null;
    }

    public List<Film> getTop(int filmsCount){
        // TODO: Получение из БД топ {filmsCount} фильмов
        return null;
    }

    public Optional<Film> findFilmByName(String filmName){
        // TODO: Поиск фильма в БД
        for (Film film : films) {
            if (film.getName().equals(filmName)) return Optional.of(film);
        }
        return Optional.empty();
    }

    public void saveGenre(){
        // TODO: Сохранение жанра в БД
    }

    public String findGenre(String genre){
        // TODO: Поиск жанра в БД
        return "";
    }
}
