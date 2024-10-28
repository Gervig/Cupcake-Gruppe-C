import app.config.ThymeleafConfig;
import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderlineMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Before;
import org.junit.Test;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntegrationTest

{
//    private Connection connection;
//    boolean actual;
//    boolean expected;
//    IntegrationTest it = new IntegrationTest();
//    BigDecimal bd = new BigDecimal(500.00);
//    User u1 = new User(1, "Casper", "Gervig", "gervig91", "gervig@gmail.com", "1234", bd, 2400, "customer");

    ConnectionPool connectionPoolTest = ConnectionPool.getInstance("postgres", "postgres", "jdbc:postgresql://localhost:5432/%s?currentSchema=test_schema", "cupcake");

    @Before
    public void setUp() throws SQLException
    {
        clearDatabase();
        initializeTestData();
    }

    @AfterEach
    public void tearDown()
    {
        // Close the connection pool after tests
        connectionPoolTest.close();
    }

    //TODO add tables back in when you run the tests
    private void clearDatabase() throws SQLException
    {
        try (Connection conn = connectionPoolTest.getConnection(); Statement stmt = conn.createStatement())
        {
            stmt.execute("DROP SCHEMA IF EXISTS test_schema CASCADE;");
            stmt.execute("CREATE SCHEMA test_schema;");
        }
    }

    private void initializeTestData() throws SQLException {
        try (Connection conn = connectionPoolTest.getConnection(); Statement stmt = conn.createStatement()) {

            // Drop the schema if it exists
            stmt.execute("DROP SCHEMA IF EXISTS test_schema CASCADE;");

            // Create the test_schema
            stmt.execute("CREATE SCHEMA IF NOT EXISTS test_schema;");

            // Create the City table
            stmt.execute("CREATE TABLE IF NOT EXISTS test_schema.City (" +
                    "postcode INTEGER PRIMARY KEY CHECK (postcode >= 1000 AND postcode <= 9999), " +
                    "city_name VARCHAR(50) NOT NULL);");

            // Create the Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS test_schema.Users (" +
                    "user_id SERIAL PRIMARY KEY, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "balance NUMERIC(10, 2) DEFAULT 0.00, " +
                    "postcode INTEGER REFERENCES test_schema.City(postcode) ON DELETE SET NULL, " +
                    "role VARCHAR(20) NOT NULL CHECK (role IN ('customer', 'admin')));");

            // Create the Bottom table
            stmt.execute("CREATE TABLE IF NOT EXISTS test_schema.Bottom (" +
                    "bottom_id SERIAL PRIMARY KEY, " +
                    "bottom_name VARCHAR(50) NOT NULL, " +
                    "price NUMERIC(5, 2) NOT NULL);");

            // Create the Topping table
            stmt.execute("CREATE TABLE IF NOT EXISTS test_schema.Topping (" +
                    "topping_id SERIAL PRIMARY KEY, " +
                    "topping_name VARCHAR(50) NOT NULL, " +
                    "price NUMERIC(5, 2) NOT NULL);");

            // Create the Orders table
            stmt.execute("CREATE TABLE IF NOT EXISTS test_schema.Orders (" +
                    "order_id SERIAL PRIMARY KEY, " +
                    "user_id INTEGER REFERENCES test_schema.Users(user_id) ON DELETE CASCADE, " +
                    "order_date TIMESTAMP DEFAULT NOW(), " +
                    "total_price NUMERIC(10, 2) NOT NULL);");

            // Create the Orderline table
            stmt.execute("CREATE TABLE IF NOT EXISTS test_schema.Orderline (" +
                    "orderline_id SERIAL PRIMARY KEY, " +
                    "order_id INTEGER REFERENCES test_schema.Orders(order_id) ON DELETE CASCADE, " +
                    "bottom_id INTEGER REFERENCES test_schema.Bottom(bottom_id) ON DELETE SET NULL, " +
                    "topping_id INTEGER REFERENCES test_schema.Topping(topping_id) ON DELETE SET NULL, " +
                    "quantity INTEGER NOT NULL CHECK (quantity > 0), " +
                    "price NUMERIC(10, 2) NOT NULL);");

            // Insert test data
            stmt.execute("INSERT INTO test_schema.City (postcode, city_name) VALUES (3700, 'Rønne');");
            stmt.execute("INSERT INTO test_schema.Users (first_name, last_name, username, email, password, balance, postcode, role) " +
                    "VALUES ('Donald', 'Duck', 'ducky', 'ducky@gmail.com', '1234', 500.00, 3700, 'customer');");
            stmt.execute("INSERT INTO test_schema.Bottom (bottom_name, price) VALUES ('Chocolate', 5.00);");
            stmt.execute("INSERT INTO test_schema.Topping (topping_name, price) VALUES ('Strawberry', 6.00);");
            stmt.execute("INSERT INTO test_schema.Orders (user_id, order_date, total_price) VALUES (1, NOW(), 11.00);");
            stmt.execute("INSERT INTO test_schema.Orderline (order_id, bottom_id, topping_id, quantity, price) " +
                    "VALUES (1, 1, 1, 1, 11.00);");
        }
    }

    @Test
    public void getAllOrderlinePerUser_ShouldReturnOrderlinesForUser() throws DatabaseException
    {
        // Use your repository to fetch data and assert
        List<Orderline> orderlines = OrderlineMapper.getAllOrderlinePerUser(1, connectionPoolTest);

        assertEquals(1, orderlines.size());
        assertEquals(1, orderlines.get(0).getOrderId());
        assertEquals(1, orderlines.get(0).getBottomId());
        assertEquals(1, orderlines.get(0).getToppingId());
    }

}
