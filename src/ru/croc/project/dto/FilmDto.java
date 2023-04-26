package ru.croc.project.dto;

import ru.croc.project.DataSource;
import ru.croc.project.models.Film;

import java.util.List;
import java.util.Optional;

public class FilmDto {
    private final DataSource dataSource;

    public FilmDto(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Film findOne(String filmName) {
        Optional<Film> foundFilm = dataSource.findFilmByName(filmName);
        return foundFilm.orElseThrow(IllegalArgumentException::new);
    }

    public List<Film> getTop(int filmsCount, String genre){
        return genre.equals("") ? dataSource.getTop(filmsCount) : dataSource.getTop(filmsCount, genre);
    }

    public void save(Film film) {
        dataSource.saveFilm(film);
    }

    public void delete(Film film) {
        dataSource.deleteFilm(film);
    }
}
