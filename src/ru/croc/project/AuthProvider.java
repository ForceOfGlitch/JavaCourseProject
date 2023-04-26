package ru.croc.project;

import ru.croc.project.models.User;
import ru.croc.project.dto.UserDto;

public class AuthProvider {
    private final UserDto userDto = new UserDto(DataSource.getDataSource());

    public AuthProvider(){}

    /**
     *
     * @param userName введённое имя пользователя
     * @return возвращает информацию о том, имеет ли пользователь права администратора
     */
    public boolean login(String userName){

        for (User user : userDto.getAllUsers()) {
            if (user.getName().equals(userName)){
                return user.isPromoted();
            }
        }

        userDto.save(new User(userName, false));
        return false;
    }
}
