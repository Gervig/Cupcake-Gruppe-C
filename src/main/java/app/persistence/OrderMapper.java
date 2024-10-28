package app.persistence;

import app.entities.Orderline;
import app.entities.Orders;
import app.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Orders> getOrdersByUserId(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM public.\"orders\" WHERE user_id = ?";

        List<Orders> orders = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Timestamp orderDate = rs.getTimestamp("order_date");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");
                orders.add(new Orders(orderId, userId, orderDate, totalPrice));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke hente ordre for bruger", e.getMessage());
        }

        return orders;
    }

    public static List<Orders> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM public.\"orders\"";

        List<Orders> orders = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                Timestamp orderDate = rs.getTimestamp("order_date");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");

                orders.add(new Orders(orderId, userId, orderDate, totalPrice));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke hente ordre", e.getMessage());
        }

        return orders;
    }

    public static void createNewOrder(int userId, List<Orderline> orderlines, BigDecimal totalPrice, ConnectionPool connectionPool) throws DatabaseException {
        String sqlOrder = "INSERT INTO public.\"orders\" (user_id, total_price) VALUES (?, ?) RETURNING order_id";
        String sqlOrderLine = "INSERT INTO public.\"orderline\" (order_id, bottom_id, topping_id, quantity, price) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement psOrder = connection.prepareStatement(sqlOrder);
                PreparedStatement psOrderLine = connection.prepareStatement(sqlOrderLine)
        ) {
            psOrder.setInt(1, userId);
            psOrder.setBigDecimal(2, totalPrice);
            ResultSet rs = psOrder.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");

                for (Orderline orderLine : orderlines) {
                    psOrderLine.setInt(1, orderId);
                    psOrderLine.setInt(2, orderLine.getBottomId());
                    psOrderLine.setInt(3, orderLine.getToppingId());
                    psOrderLine.setInt(4, orderLine.getQuantity());
                    psOrderLine.setBigDecimal(5, orderLine.getPrice());
                    psOrderLine.addBatch();
                }

                psOrderLine.executeBatch();
            } else {
                throw new DatabaseException("Kunne ikke oprette ordre");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke oprette ordre", e.getMessage());
        }
    }


    private static List<Orderline> getOrderLinesByOrderId(int orderId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM public.\"orderline\" WHERE order_id = ?";

        List<Orderline> orderlines = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int orderLineId = rs.getInt("orderline_id");
                int bottomId = rs.getInt("bottom_id");
                int toppingId = rs.getInt("topping_id");
                int quantity = rs.getInt("quantity");
                BigDecimal price = rs.getBigDecimal("price");

                orderlines.add(new Orderline(orderLineId, orderId, bottomId, toppingId, quantity, price));
            }
        }

        return orderlines;
    }
}
