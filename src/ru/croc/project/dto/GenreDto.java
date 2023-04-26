package ru.croc.project.dto;

import ru.croc.project.DataSource;

public class GenreDto {
    private final DataSource dataSource;

    public GenreDto(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(String genre){
        // TODO: Сохранение жанра в БД
        dataSource.saveGenre();
    }

    public boolean isGenreExists(String genre){
        // TODO: Существует ли жанр в БД
        return !dataSource.findGenre(genre).equals("");
    }
}
