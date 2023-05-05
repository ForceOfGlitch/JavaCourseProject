package ru.croc.project.models;

import java.util.List;

public class Film {

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    private int id;

    private String name;

    private List<String> genres;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Film(){}
    public Film(int id, String name, List<String> genres){
        this.id = id;
        this.name = name;
        this.genres = genres;
    }

    @Override
    public String toString(){
        String template = "Film = {Id: %d, Name: %s, Genres: %s }";
        StringBuilder genresString = new StringBuilder();

        for (int i = 0; i < genres.size(); i++) {
            genresString.append(genres.get(i));
            if (i < genres.size() - 1){
                genresString.append(", ");
            }
        }

        return String.format(template, id, name, genresString);
    }
}
