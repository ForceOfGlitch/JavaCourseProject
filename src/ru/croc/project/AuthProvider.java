package ru.croc.project;

import ru.croc.project.models.User;
import ru.croc.project.dao.UserDao;

public class AuthProvider {
    private final UserDao userDao = new UserDao(DataSource.getDataSource());

    public AuthProvider(){}

    /**
     *
     * @param userName введённое имя пользователя
     * @return возвращает информацию о том, имеет ли пользователь права администратора
     */
    public boolean login(String userName){

        for (User user : userDao.getAllUsers()) {
            if (user.getName().equals(userName)){
                return user.isPromoted();
            }
        }

        userDao.save(new User(0, userName, false));
        return false;
    }
}
