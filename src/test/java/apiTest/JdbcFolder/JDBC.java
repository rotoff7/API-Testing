package apiTest.JdbcFolder;

import org.junit.jupiter.api.Assertions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBC extends BaseTest {

    public static void deleteRow(String name, String type) {
        BaseTest.connect();
        String sqlDel = ("DELETE FROM food WHERE food_name = ? AND food_type = ?;");
        // Получаем данные только что добавленного товара
        try (PreparedStatement ps = connection.prepareStatement(sqlDel)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.executeUpdate();
            BaseTest.connectClose();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
