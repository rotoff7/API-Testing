package apiTest.JdbcFolder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class JDBC extends BaseTest {
    @Test
    @DisplayName("sql test-case#1")
    void test1() {
//        String sqlInsert = ("INSERT INTO food (food_name, food_type, food_exotic) VALUES (?, ?, ?);");
        String sqlSelect = ("SELECT * FROM food WHERE food_name = ? AND food_type = ?;");
        // Получаем данные только что добавленного товара
        try (PreparedStatement ps = connection.prepareStatement(sqlSelect)) {
            ps.setString(1, "New Fruit");
            ps.setString(2, "FRUIT");
            try (ResultSet rs = ps.executeQuery()) {
                rs.last();
                String name = rs.getString("food_name");
                String type = rs.getString("food_type");
                int exotic = rs.getInt("food_exotic");
//                        Проверяем корректность данных. Надеюсь тут правильность установки id ассертить не надо.
                Assertions.assertEquals("New Fruit", name, "Wrong name");
                Assertions.assertEquals("FRUIT", type, "Wrong type");
                Assertions.assertEquals(1, exotic, "Wrong exotic type");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
