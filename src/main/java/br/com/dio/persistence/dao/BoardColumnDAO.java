package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BoardColumnCategoryEnum;
import br.com.dio.persistence.entity.BoardColumnEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardColumnDAO {
    private final Connection connection;

    public BoardColumnEntity create(final BoardColumnEntity boardColumn) throws SQLException {
        String sql = "INSERT INTO board_columns (name, `order`, category, board_id) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, boardColumn.getName());
            ps.setInt(i++, boardColumn.getOrder());
            ps.setString(i++, boardColumn.getCategory().name());
            ps.setLong(i, boardColumn.getBoard().getId());
            ps.executeUpdate();

            if (ps instanceof StatementImpl impl) {
                boardColumn.setId(impl.getLastInsertID());
            }
            return boardColumn;
        }
    }

    public List<BoardColumnEntity> findBoardById(Long id) throws SQLException {
        List<BoardColumnEntity> boardColumns = new ArrayList<>();
        String sql = "SELECT * FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY `order`;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                var boardColumn = new BoardColumnEntity();
                boardColumn.setId(rs.getLong("id"));
                boardColumn.setName(rs.getString("name"));
                boardColumn.setOrder(rs.getInt("order"));
                boardColumn.setCategory(BoardColumnCategoryEnum.fromString(rs.getString("category")));
                boardColumns.add(boardColumn);
            }
            return boardColumns;
        }
    }
}
