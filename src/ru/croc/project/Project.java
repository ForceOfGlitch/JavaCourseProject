package ru.croc.project;

import ru.croc.project.models.Film;
import ru.croc.project.models.User;
import ru.croc.project.dto.FilmDto;
import ru.croc.project.dto.GenreDto;
import ru.croc.project.dto.ScoreDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Project {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AuthProvider authProvider = new AuthProvider();

        FilmDto filmDto = new FilmDto(DataSource.getDataSource());
        GenreDto genreDto = new GenreDto(DataSource.getDataSource());
        ScoreDto scoreDto = new ScoreDto(DataSource.getDataSource());

        System.out.println("Введите имя пользователя");
        String userName = scanner.nextLine();
        boolean isPromoted = authProvider.login(userName);
        CommandFilter commandFilter = new CommandFilter(isPromoted, 2, 2);

        int currentCommand;

        while(true){
            System.out.println("Перечень доступных команд (ввести нужный номер):");
            System.out.println("1. Поставить оценку фильму");
            System.out.println("2. Сформировать топ по конкретному жанру");
            if (isPromoted) {
                System.out.println("3. Добавить фильм");
                System.out.println("4. Удалить фильм");
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
                            System.out.println("Напишите название фильма");
                            film = filmDto.findOne(scanner.nextLine());
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

                    scoreDto.saveScore(new User(userName, isPromoted), film, Integer.parseInt(commandLine));
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

                    System.out.println("На данный момент система не формирует рейтинг по причине отсутствия существования оценок (нет БД)");
                    System.out.println(new FilmDto(DataSource.getDataSource()).getTop(filmsCount, genre));
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

                        if (!genreDto.isGenreExists(currGenre)){
                            System.out.println("Такого жанра не существует");
                            continue;
                        }

                        genres.add(currGenre);
                        System.out.println("Жанр добавлен к фильму");
                    }

                    filmDto.save(new Film(filmName, genres));
                    System.out.println("Фильм успешно добавлен");
                    break;
                }

                case (4): {
                    Film film;

                    while(true){
                        try{
                            System.out.println("Напишите название фильма для удаления");
                            film = filmDto.findOne(scanner.nextLine());
                            break;
                        } catch (IllegalArgumentException exception) {
                            System.out.println("Фильм с таким названием не найден");
                        }
                    }

                    filmDto.delete(film);
                    System.out.println("Фильм успешно удалён");
                    break;
                }

                default:{
                    System.out.println("Такой команды не предусмотрено");
                }
            }
        }
    }
}
