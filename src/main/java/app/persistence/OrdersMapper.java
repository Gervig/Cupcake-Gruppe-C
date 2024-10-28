package app.persistence;

import app.entities.Orders;
import app.exceptions.DatabaseException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersMapper {

    public static List<Orders> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<Orders> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalPrice(rs.getBigDecimal("total_price"));

                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching orders from database", e.getMessage());
        }
        return orders;
    }
}
