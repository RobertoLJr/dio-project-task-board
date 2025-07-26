package br.com.dio.persistence.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class ConnectionConfig {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost/board";
        String username = "dev";
        String password = "";
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);

        return connection;
    }
}
