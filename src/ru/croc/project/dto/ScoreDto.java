package ru.croc.project.dto;

import ru.croc.project.DataSource;
import ru.croc.project.models.Film;
import ru.croc.project.models.User;

public class ScoreDto {
    private final DataSource dataSource;

    public ScoreDto(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveScore(User user, Film film, int score){
        // TODO: Сохранение оценки фильма {film} от пользователя {user}
    }
}
