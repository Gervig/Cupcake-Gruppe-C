package app.persistence;

import app.entities.Order;
import app.entities.OrderLine;
import app.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Order> getOrdersByUserId(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM public.\"orders\" WHERE user_id = ?";

        List<Order> orders = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");

                List<OrderLine> orderLines = getOrderLinesByOrderId(orderId, connection);

                orders.add(new Order(orderId, userId, totalPrice, orderLines));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke hente ordre for bruger", e.getMessage());
        }

        return orders;
    }

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM public.\"orders\"";

        List<Order> orders = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");

                List<OrderLine> orderLines = getOrderLinesByOrderId(orderId, connection);

                orders.add(new Order(orderId, userId, totalPrice, orderLines));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke hente ordre", e.getMessage());
        }

        return orders;
    }

    public static void createNewOrder(int userId, List<OrderLine> orderLines, BigDecimal totalPrice, ConnectionPool connectionPool) throws DatabaseException {
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

                for (OrderLine orderLine : orderLines) {
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


    private static List<OrderLine> getOrderLinesByOrderId(int orderId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM public.\"orderline\" WHERE order_id = ?";

        List<OrderLine> orderLines = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int orderLineId = rs.getInt("orderline_id");
                int bottomId = rs.getInt("bottom_id");
                int toppingId = rs.getInt("topping_id");
                int quantity = rs.getInt("quantity");
                BigDecimal price = rs.getBigDecimal("price");

                orderLines.add(new OrderLine(orderLineId, orderId, bottomId, toppingId, quantity, price));
            }
        }

        return orderLines;
    }
}
