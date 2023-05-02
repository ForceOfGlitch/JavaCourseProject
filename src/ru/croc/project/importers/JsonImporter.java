package ru.croc.project.importers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.croc.project.DataSource;
import ru.croc.project.dao.FilmDao;
import ru.croc.project.dao.GenreDao;
import ru.croc.project.models.Film;
import ru.croc.project.models.Genre;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JsonImporter {
    /**
     * Импортирует из json файла жанры и вставляет их в БД. В случае, если какой-либо из жанров нарушает правила ввода, то
     * система выдаёт оповещение об ошибке и переходит к следующему жанру в файле
     */
    private void importGenresFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GenresList genresList = objectMapper.readValue(new File("src/ru/croc/project/genres.json"), GenresList.class);
            GenreDao genreDao = new GenreDao(DataSource.getDataSource());

            for (Genre genre :
                    genresList.getGenres()) {
                genreDao.save(genre.getName());
            }
        } catch (IOException e){
            System.out.println("Ошибка считывания json " + e.getMessage());
        }
    }

    /**
     * Импортирует из json файла фильмы и вставляет их в БД. В случае, если какой-либо из фильмов нарушает правила ввода, то
     * система выдаёт оповещение об ошибке и переходит к следующему фильму в файле
     */
    private void importFilmsFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FilmList filmList = objectMapper.readValue(new File("src/ru/croc/project/films.json"), FilmList.class);
            FilmDao filmDao = new FilmDao(DataSource.getDataSource());

            for (Film film :
                    filmList.getFilms()) {
                filmDao.save(film);
            }

        } catch (IOException e){
            System.out.println("Ошибка считывания json " + e.getMessage());
        }
    }

    /**
     * Метод непосредственного вызова импорта
     */
    public void doImport(){
        importGenresFromJson();
        importFilmsFromJson();
    }
}