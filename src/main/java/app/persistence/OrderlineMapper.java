package app.persistence;

import app.entities.Orderline;
import app.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderlineMapper {

    public static List<Orderline> getAllOrderlinePerUser(int userId, ConnectionPool connectionPool) throws DatabaseException {
        List<Orderline> orders = new ArrayList<>();
        String sql = "SELECT Orderline.orderline_id, Orderline.order_id, Orderline.bottom_id, Bottom.bottom_name, "
                + "Orderline.topping_id, Topping.topping_name, Orderline.quantity, Orderline.price "
                + "FROM Orderline "
                + "JOIN Orders ON Orderline.order_id = Orders.order_id "
                + "JOIN Users ON Orders.user_id = Users.user_id "
                + "JOIN Bottom ON Orderline.bottom_id = Bottom.bottom_id "
                + "JOIN Topping ON Orderline.topping_id = Topping.topping_id "
                + "WHERE Users.user_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Orderline orderLine = mapResultSetToOrderline(rs);
                orders.add(orderLine);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af brugerens ordrelinjer!", e.getMessage());
        }
        return orders;
    }

    public static List<Orderline> getAllOrderlines(ConnectionPool connectionPool) throws DatabaseException {
        List<Orderline> orders = new ArrayList<>();

        String sql = "SELECT Orderline.orderline_id, " +
                "Orderline.order_id, " +
                "Orderline.bottom_id, " +
                "Bottom.bottom_name, " +
                "Orderline.topping_id, " +
                "Topping.topping_name, " +
                "Orderline.quantity, " +
                "Orderline.price " +
                "FROM Orderline " +
                "JOIN Orders ON Orderline.order_id = Orders.order_id " +
                "JOIN Bottom ON Orderline.bottom_id = Bottom.bottom_id " +
                "JOIN Topping ON Orderline.topping_id = Topping.topping_id";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int orderlineId = rs.getInt("orderline_id");
                int orderId = rs.getInt("order_id");
                int bottomId = rs.getInt("bottom_id");
                String bottomName = rs.getString("bottom_name");
                int toppingId = rs.getInt("topping_id");
                String toppingName = rs.getString("topping_name");
                int quantity = rs.getInt("quantity");
                BigDecimal price = rs.getBigDecimal("price");
                Orderline orderLine = new Orderline(orderlineId, orderId, bottomId, toppingId, quantity, price);
                orders.add(orderLine);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching orderlines from database: " + e.getMessage());
        }

        return orders;
    }


    private static Orderline mapResultSetToOrderline(ResultSet rs) throws SQLException {
        int orderlineId = rs.getInt("orderline_id");
        int orderId = rs.getInt("order_id");
        int bottomId = rs.getInt("bottom_id");
        String bottomName = rs.getString("bottom_name");
        int toppingId = rs.getInt("topping_id");
        String toppingName = rs.getString("topping_name");
        int quantity = rs.getInt("quantity");
        BigDecimal price = rs.getBigDecimal("price");

        return new Orderline(orderlineId, orderId, bottomId, toppingId, quantity, price);
    }
}
