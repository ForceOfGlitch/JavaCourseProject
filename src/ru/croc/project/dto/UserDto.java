package ru.croc.project.dto;

import ru.croc.project.DataSource;
import ru.croc.project.models.User;

import java.util.ArrayList;
import java.util.Optional;

public class UserDto {

    private final DataSource dataSource;

    public UserDto(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<User> getAllUsers() {
        return dataSource.getUsers();
    }

    public User findOne(String name) {
        Optional<User> foundUser = dataSource.findUserByName(name);
        return foundUser.orElseThrow(IllegalArgumentException::new);
    }

    public void save(User user) {
        dataSource.saveUser(user);
    }

}
