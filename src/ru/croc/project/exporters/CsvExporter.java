package ru.croc.project.exporters;

import ru.croc.project.DataSource;
import ru.croc.project.dao.FilmDao;
import ru.croc.project.dao.GenreDao;
import ru.croc.project.models.Film;
import ru.croc.project.models.Genre;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {
    /**
     * Метод экспорта всех жанров из БД в csv файл
     * @throws IOException
     */
    private void exportGenresToCsv() throws IOException {
        FileWriter csvWriter = new FileWriter("src/ru/croc/project/genres.csv");
        csvWriter.append("name\n");

        GenreDao genreDao = new GenreDao(DataSource.getDataSource());
        List<Genre> genres = genreDao.getAllGenres();

        for (Genre object : genres) {
            csvWriter.append(object.getName());
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();
    }

    /**
     * Метод экспорта всех названий фильмов из БД в csv файл
     * @throws IOException
     */
    private void exportFilmsToCsv() throws IOException {
        FileWriter csvWriter = new FileWriter("src/ru/croc/project/films.csv");
        csvWriter.append("name\n");

        FilmDao filmDao = new FilmDao(DataSource.getDataSource());
        List<Film> films = filmDao.getAllFilmsNames();

        for (Film film : films) {
            csvWriter.append(film.getName());
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();
    }

    /**
     * Метод непосредственного вызова экспорта
     */
    public void exportToCsv() {
        try {
            exportGenresToCsv();
            exportFilmsToCsv();
        } catch (IOException exception){
            System.out.println("Ошибка при экспорте в csv: " + exception.getMessage());
        }
    }
}
