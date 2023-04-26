package ru.croc.project.models;

import java.util.List;

public class Film {

    private final String name;

    private final List<String> genres;

    public String getName() {
        return name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Film(String name, List<String> genres){
        this.name = name;
        this.genres = genres;
    }

    @Override
    public String toString(){
        return "Film{ " +
                "Name: " + name +
                "Genre: " + genres +
                " }";
    }
}
