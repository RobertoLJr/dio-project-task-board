package br.com.dio.ui;

import br.com.dio.persistence.config.ConnectionConfig;
import br.com.dio.persistence.entity.BoardColumnCategoryEnum;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    
    public void execute() throws SQLException {
        System.out.println(">> Welcome to the Task Board <<");
        System.out.println("Select an option:");
        var option = -1;
        while (true) {
            System.out.println("1 - Create a new board");
            System.out.println("2 - Select an existing board");
            System.out.println("3 - Delete a board");
            System.out.println("4 - Exit");
            System.out.print("User Option: ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void createBoard() throws SQLException {
        var entity = new BoardEntity();
        System.out.print("Inform the name of the board: ");
        entity.setName(scanner.nextLine());

        System.out.print("How many additional columns would you like to add (enter 0 if none)? ");
        int additionalColumn = scanner.nextInt();
        scanner.nextLine();

        List<BoardColumnEntity> columns = new ArrayList<>();
        System.out.print(">> Inform name of the first column: ");
        var startingColumnName = scanner.nextLine();
        var startingColumn = createColumn(startingColumnName, BoardColumnCategoryEnum.INITIAL, 0);
        columns.add(startingColumn);

        for (int i = 0; i < additionalColumn; i++) {
            System.out.print(">> Inform name of pending asks column: ");
            var pendingColumnName = scanner.nextLine();
            var pendingColumn = createColumn(pendingColumnName, BoardColumnCategoryEnum.PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.print(">> Inform name of the final column: ");
        var lastColumnName = scanner.nextLine();
        var lastColumn = createColumn(lastColumnName, BoardColumnCategoryEnum.FINAL, additionalColumn + 1);
        columns.add(lastColumn);

        System.out.print(">> Inform name of the board cancelling column: ");
        var cancelColumnName = scanner.nextLine();
        var cancelColumn = createColumn(cancelColumnName, BoardColumnCategoryEnum.CANCEL, additionalColumn + 2);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);
        try (Connection connection = ConnectionConfig.getConnection()) {
            var service = new BoardService(connection);
            service.create(entity);
        }
    }

    private void selectBoard() throws SQLException {
        System.out.print("Inform the id of the board to be selected: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        try (Connection connection = ConnectionConfig.getConnection()) {
            BoardQueryService queryService = new BoardQueryService(connection);
            Optional<BoardEntity> optional = queryService.findById(id);
            optional.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.println(">> Board not found.")
            );
        }
    }

    private void deleteBoard() {
        System.out.print("Inform the id of the board to be deleted: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        try (Connection connection = ConnectionConfig.getConnection()) {
            var service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf(">> Board %s deleted successfully.%n", id);
            } else {
                System.out.printf(">> Board %s not found.%n", id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnCategoryEnum category, final int order) {
        BoardColumnEntity boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setCategory(category);
        boardColumn.setOrder(order);
        return boardColumn;
    }
}
