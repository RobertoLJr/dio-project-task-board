package br.com.dio.app;

import br.com.dio.persistence.config.ConnectionConfig;
import br.com.dio.persistence.migration.MigrationStrategy;
import br.com.dio.ui.MainMenu;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        try (var conn = ConnectionConfig.getConnection()) {
            new MigrationStrategy(conn).executeMigration();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        new MainMenu().execute();
    }
}