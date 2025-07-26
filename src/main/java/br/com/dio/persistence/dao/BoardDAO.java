package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BoardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {
    private final Connection connection;

    public BoardEntity create(final BoardEntity board) throws SQLException {
        String sql = "INSERT INTO boards (name) VALUES (?);";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, board.getName());
            statement.executeUpdate();
            if (statement instanceof StatementImpl impl) {
                board.setId(impl.getLastInsertID());
            }
        }

        return board;
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        String sql = "SELECT * FROM boards WHERE id = ?;";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                var board = new BoardEntity();
                board.setId(resultSet.getLong("id"));
                board.setName(resultSet.getString("name"));
                return Optional.of(board);
            }
            return Optional.empty();
        }
    }

    public void delete(final Long id) throws SQLException {
        String sql = "DELETE FROM boards WHERE id = ?;";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public boolean exists(final Long id) throws SQLException {
        String sql = "SELECT 1 FROM boards WHERE id = ?;";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            return statement.getResultSet().next();
        }
    }
}
