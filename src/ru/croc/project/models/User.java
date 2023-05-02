package ru.croc.project.models;

public class User {

    private int id;

    private String name;

    private boolean isPromoted;

    public boolean isPromoted() {
        return isPromoted;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public User(){}

    public User(int id, String name, boolean isPromoted){
        this.id = id;
        this.name = name;
        this.isPromoted = isPromoted;
    }

    @Override
    public String toString(){
        return String.format("User = {Name: %s}", name);
    }

}
