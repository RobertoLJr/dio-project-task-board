package br.com.dio.ui;

import br.com.dio.dto.BoardColumnInfoDTO;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.persistence.entity.CardEntity;
import br.com.dio.service.BoardColumnQueryService;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.CardQueryService;
import br.com.dio.service.CardService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private final BoardEntity entity;

    public void execute() {
        try {
            System.out.printf("Welcome to Board %s, select an option: ", entity.getId());
            var option = -1;
            while (option != 9) {
                System.out.println("1 - Create a new card");
                System.out.println("2 - Move a card");
                System.out.println("3 - Block a card");
                System.out.println("4 - Unblock a card");
                System.out.println("5 - Cancel a card");
                System.out.println("6 - View board");
                System.out.println("7 - View column with cards");
                System.out.println("8 - View card");
                System.out.println("9 - Back to previous menu");
                System.out.println("10 - Exit");
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Backing to previous menu...");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() throws SQLException{
        var card = new CardEntity();
        System.out.print("Inform the card title: ");
        card.setTitle(scanner.nextLine());
        System.out.print("Inform the card description: ");
        card.setDescription(scanner.nextLine());
        card.setBoardColumn(entity.getInitialColumn());
        try(var connection = getConnection()){
            new CardService(connection).create(card);
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.print("Inform the ID of the card to be moved to the next column: ");
        var cardId = scanner.nextLong();
        scanner.nextLine();
        var boardColumnsInfo = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getCategory()))
                .toList();
        try(var connection = getConnection()){
            new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void blockCard() throws SQLException {
        System.out.print("Inform the ID of the card to be blocked: ");
        var cardId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Inform the cause of the block: ");
        var cause = scanner.nextLine();
        var boardColumnsInfo = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getCategory()))
                .toList();
        try(var connection = getConnection()){
            new CardService(connection).block(cardId, cause, boardColumnsInfo);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void unblockCard() throws SQLException {
        System.out.print("Inform the ID of the card to be unblocked: ");
        var cardId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Inform the cause of the unblock: ");
        var cause = scanner.nextLine();
        try(var connection = getConnection()){
            new CardService(connection).unblock(cardId, cause);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void cancelCard() throws SQLException {
        System.out.print("Inform the ID of the card to be cancelled: ");
        var cardId = scanner.nextLong();
        scanner.nextLine();
        var cancelColumn = entity.getCancelColumn();
        var boardColumnsInfo = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getCategory()))
                .toList();
        try(var connection = getConnection()){
            new CardService(connection).cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try(var connection = getConnection()){
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach(c ->
                        System.out.printf("Column [%s] Category: [%s] has %s cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    private void showColumn() throws SQLException {
        var columnsIds = entity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        var selectedColumnId = -1L;
        while (!columnsIds.contains(selectedColumnId)){
            System.out.printf("Pick a column from Board %s by id\n", entity.getName());
            entity.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getCategory()));
            selectedColumnId = scanner.nextLong();
            scanner.nextLine();
        }
        try(var connection = getConnection()){
            var column = new BoardColumnQueryService(connection).findById(selectedColumnId);
            column.ifPresent(co -> {
                System.out.printf("Column %s Category %s\n", co.getName(), co.getCategory());
                co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescription: %s",
                        ca.getId(), ca.getTitle(), ca.getDescription()));
            });
        }
    }

    private void showCard() throws SQLException {
        System.out.print("Inform the ID of the card to be shown: ");
        var selectedCardId = scanner.nextLong();
        scanner.nextLine();
        try(var connection  = getConnection()){
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(
                            c -> {
                                System.out.printf("Card %s - %s.\n", c.id(), c.title());
                                System.out.printf("Description: %s\n", c.description());
                                System.out.println(c.blocked() ?
                                        "Blocked. Motivo: " + c.blockCause() :
                                        "Not Blocked");
                                System.out.printf("Has been blocked %s times\n", c.blocksAmount());
                                System.out.printf("Is in column %s - %s\n", c.columnId(), c.columnName());
                            },
                            () -> System.out.printf("There are no cards with ID %s\n", selectedCardId));
        }
    }

}