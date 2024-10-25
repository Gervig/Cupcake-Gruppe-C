package app.persistence;

import app.entities.Orderline;
import app.entities.Orders;
import app.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderlineMapper
{
    public static List<Orderline> getAllOrderlinePerUser(int userId, ConnectionPool connectionPool) throws DatabaseException
    {
        List<Orderline> orderlineList = new ArrayList<>();

        // SQL join query that joins the orderline and orders tables,
        // so you can search orderlines for a given user
        String sql = "SELECT Orderline.orderline_id, " +
                "Orderline.order_id, " +
                "Orderline.bottom_id, " +
                "Bottom.bottom_name, " +
                "Orderline.topping_id, " +
                "Topping.topping_name, " +
                "Orderline.quantity, " +
                "Orderline.price " +
                "FROM Orderline " +
                "JOIN Orders " +
                "ON Orderline.order_id = Orders.order_id " +
                "JOIN Users ON Orders.user_id = Users.user_id " +
                "JOIN Bottom ON Orderline.bottom_id = Bottom.bottom_id " +
                "JOIN Topping ON Orderline.topping_id = Topping.topping_id " +
                "WHERE Users.user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                // bottomName and toppingName currently not used
                int orderlineId = rs.getInt("orderline_id");
                int orderId = rs.getInt("order_id");
                int bottomId = rs.getInt("bottom_id");
                String bottomName = rs.getString("bottom_name");
                int toppingId = rs.getInt("topping_id");
                String toppingName = rs.getString("topping_name");
                int quantity = rs.getInt("quantity");
                BigDecimal price = rs.getBigDecimal("price");
                orderlineList.add(new Orderline(orderlineId, orderId, bottomId, toppingId, quantity, price));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl!",e.getMessage());
    }

        return orderlineList;
    }
}
