package apiTest.JdbcFolder;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class BaseTest {
    protected static Connection connection;

    static void connect() {
        DataSource source = getDataSource();
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void connectClose() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSource getDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:tcp://localhost:9092/mem:testdb");
        dataSource.setUser("user");
        dataSource.setPassword("pass");
        return dataSource;
    }
}