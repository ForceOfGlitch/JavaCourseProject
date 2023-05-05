package ru.croc.project;

import java.sql.*;


public class DataSource {
    private static DataSource dataSource;
    private Connection connection;

    private DataSource() throws SQLException {
        String url = "jdbc:h2:tcp://localhost:9092/~/Desktop/db/test";

        try {
            connection = DriverManager.getConnection(url, "1", "1");
            System.out.println("Установили соединение с БД");
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
    }

    /**
     * Метод получения подключения для формирования запросов
     * @return
     */
    public Connection getConnection(){
        return connection;
    }
    /**
     * Получение источника данных
     * @return новый источник данных, если ещё не был создан, либо ссылку на существующий
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            try {
                dataSource = new DataSource();
            } catch (SQLException sqlException){
                System.out.println("Ошибка подключения к БД");
            }
            return dataSource;
        }
        return dataSource;
    }
}
