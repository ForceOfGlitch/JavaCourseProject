package ru.croc.project.models;

public class User {

    private String name;

    private boolean isPromoted;

    public boolean isPromoted() {
        return isPromoted;
    }

    public String getName() {
        return name;
    }

    public User(){}

    public User(String name, boolean isPromoted){
        this.name = name;
        this.isPromoted = isPromoted;
    }

    @Override
    public String toString(){
        return "User{ " +
                "Name: " + name +
                " }";
    }

}
