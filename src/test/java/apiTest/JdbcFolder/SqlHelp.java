package apiTest.JdbcFolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static apiTest.JdbcFolder.BaseTest.connection;

public class SqlHelp {
    static void delRow(int id) {
        String sqlRequest = "delete from FOOD where food_id = (?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlRequest);) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
