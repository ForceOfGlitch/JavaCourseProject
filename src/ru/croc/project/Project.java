package ru.croc.project;

import ru.croc.project.dao.UserDao;
import ru.croc.project.exporters.CsvExporter;
import ru.croc.project.importers.JsonImporter;
import ru.croc.project.models.Film;
import ru.croc.project.models.User;
import ru.croc.project.dao.FilmDao;
import ru.croc.project.dao.GenreDao;
import ru.croc.project.dao.ScoreDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Project {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AuthProvider authProvider = new AuthProvider();

        FilmDao filmDao = new FilmDao(DataSource.getDataSource());
        GenreDao genreDao = new GenreDao(DataSource.getDataSource());
        ScoreDao scoreDao = new ScoreDao(DataSource.getDataSource());
        UserDao userDao = new UserDao(DataSource.getDataSource());

        System.out.println("Введите имя пользователя");
        String userName = scanner.nextLine();
        boolean isPromoted = authProvider.login(userName);
        CommandFilter commandFilter = new CommandFilter(isPromoted, 2, 4);

        int currentCommand;

        mainLoop : while(true){
            System.out.println("Перечень доступных команд (ввести нужный номер):");
            System.out.println("1. Поставить оценку фильму");
            System.out.println("2. Сформировать топ по конкретному жанру");
            if (isPromoted) {
                System.out.println("3. Добавить фильм");
                System.out.println("4. Удалить фильм");
                System.out.println("5. Импорт фильмов и жанров из json");
                System.out.println("6. Экспорт фильмов и жанров в csv");
            }

            String commandLine = scanner.nextLine();

            if(!commandFilter.isNonNegativeNumber(commandLine) || !commandFilter.isCommandExists(currentCommand = Integer.parseInt(commandLine))){
                System.out.println("Такой команды не предусмотрено");
                continue;
            }

            switch (currentCommand){
                case (1): {
                    Film film;

                    while(true){
                        try{
                            System.out.println("Напишите название фильма (пустая строка выход)");
                            String inputString = scanner.nextLine();
                            if (inputString.equals("")) continue mainLoop;
                            film = filmDao.findOne(inputString);
                            break;
                        } catch (IllegalArgumentException exception) {
                            System.out.println("Фильм с таким названием не найден");
                        }
                    }

                    System.out.println("Поставьте оценку от 0 до 5");
                    commandLine = scanner.nextLine();
                    while (!commandFilter.isNonNegativeNumber(commandLine) || (Integer.parseInt(commandLine) < 0 || Integer.parseInt(commandLine) > 5)) {
                        System.out.println("Введите корректную оценку");
                        commandLine = scanner.nextLine();
                    }

                    scoreDao.saveScore(new User(userDao.findOne(userName).getId(), userName, isPromoted), film, Integer.parseInt(commandLine));
                    break;
                }
                case (2): {
                    int filmsCount;
                    String genre;
                    System.out.println("Напишите требуемое количество фильмов");
                    commandLine = scanner.nextLine();

                    while (!commandFilter.isNonNegativeNumber(commandLine)) {
                        System.out.println("Введите корректное число фильмов");
                        commandLine = scanner.nextLine();
                    }

                    filmsCount = Integer.parseInt(commandLine);

                    System.out.println("Напишите жанр (необязательно)");
                    genre = scanner.nextLine();

                    new FilmDao(DataSource.getDataSource()).printTop(filmsCount, genre);
                    break;
                }
                case (3): {
                    System.out.println("Напишите название фильма");
                    String filmName = scanner.nextLine();
                    List<String> genres = new ArrayList<>();
                    System.out.println("Введите жанры фильма построчно (пустая строка означает конец ввода жанров)");
                    while (true){
                        String currGenre = scanner.nextLine();
                        if (currGenre.equals("")) break;

                        if (!genreDao.isGenreExists(currGenre)){
                            System.out.println("Такого жанра не существует");
                            continue;
                        }

                        genres.add(currGenre);
                        System.out.println("Жанр добавлен к фильму");
                    }

                    filmDao.save(new Film(0, filmName, genres));
                    System.out.println("Фильм успешно добавлен");
                    break;
                }

                case (4): {
                    Film film;

                    while(true){
                        try{
                            System.out.println("Напишите название фильма для удаления");
                            film = filmDao.findOne(scanner.nextLine());
                            break;
                        } catch (IllegalArgumentException exception) {
                            System.out.println("Фильм с таким названием не найден");
                        }
                    }

                    filmDao.delete(film);
                    System.out.println("Фильм успешно удалён");
                    break;
                }

                case (5): {
                    JsonImporter jsonImporter = new JsonImporter();
                    jsonImporter.doImport();
                    System.out.println("Произведён импорт фильмов и жанров из json");
                    break;
                }

                case (6): {
                    CsvExporter csvExporter = new CsvExporter();
                    csvExporter.exportToCsv();
                    System.out.println("Произведён экспорт фильмов и жанров в csv формат");
                    break;
                }

                default:{
                    System.out.println("Такой команды не предусмотрено");
                }
            }
        }
    }
}
