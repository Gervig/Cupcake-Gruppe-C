import app.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class IntegrationTest
{
    boolean actual;
    boolean expected;
    IntegrationTest it = new IntegrationTest();
    BigDecimal bd = new BigDecimal(500.00);
    User u1 = new User(1, "Casper", "Gervig", "gervig91", "gervig@gmail.com", "1234", bd, 2400, "customer");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws SQLException
    {
        // Optionally, you can clear the database before each test
        clearDatabase();
        initializeTestData();
    }

    @AfterEach
    public void tearDown() throws SQLException
    {
        clearDatabase();  // Clean up after each test
    }

    private void clearDatabase() throws SQLException
    {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement())
        {
            stmt.execute("DROP SCHEMA IF EXISTS test_schema CASCADE;");
            stmt.execute("CREATE SCHEMA test_schema;");
        }
    }

    private void initializeTestData() throws SQLException {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO test_schema.City (postcode, city_name) VALUES (3700, 'Rønne');");
            stmt.execute("INSERT INTO test_schema.Users (user_id, first_name, last_name, username, email, password, balance, postcode, role) VALUES (1, 'Donald', 'Duck', 'ducky', 'ducky@gmail.com', '1234', 500.00, 3700, 'customer');");
            stmt.execute("INSERT INTO test_schema.Bottom (bottom_id, bottom_name, price) VALUES (1, 'Chocolate', 5.00);");
            stmt.execute("INSERT INTO test_schema.Topping (topping_id, topping_name, price) VALUES (1, 'Strawberry', 6.00);");
            stmt.execute("INSERT INTO test_schema.Orders (order_id, user_id, order_date, total_price) VALUES (1, 1, NOW(), 11.00);");
            stmt.execute("INSERT INTO test_schema.Orderline (orderline_id, order_id, bottom_id, topping_id, quantity, price) VALUES (1, 1, 1, 1, 1, 11.00);");
        }
    }

}
