package app.persistence;

import app.entities.Bottoms;
import app.entities.Orders;
import app.entities.Toppings;
import app.exceptions.DatabaseException;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper
{
    public static List<Bottoms> getBottoms(ConnectionPool connectionPool) throws DatabaseException
    {

        List<Bottoms> bottomsList = new ArrayList<>();
        String sql = "SELECT * FROM bottom";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                bottomsList.add(new Bottoms(rs.getInt("bottom_id"), rs.getString("bottom_name"), rs.getBigDecimal("price")));
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Der skete en fejl");
        }
        return bottomsList;
    }

    public static List<Toppings> getToppings(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Toppings> toppingsList = new ArrayList<>();
        String sql = "SELECT * FROM topping";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                toppingsList.add(new Toppings(rs.getInt("topping_id"), rs.getString("topping_name"), rs.getBigDecimal("price")));
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Der skete en fejl");
        }
        return toppingsList;
    }

    public static List<Orders> getOrders(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                ordersList.add(new Orders(rs.getInt("order_id"), rs.getInt("user_id"), rs.getTimestamp("order_date"), rs.getBigDecimal("total_price")));
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Der skete en fejl");
        }
        return ordersList;
    }

    public static void createOrderLine(int bottomId, int toppingId, int quantity, int userId, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "INSERT INTO public.Orderline (order_id, bottom_id, topping_id, quantity, price) VALUES (?, ?, ?, ?, ?)";
        // Variable to hold the order ID
        int orderId;

        try (Connection connection = connectionPool.getConnection())
        {
            orderId = getOrCreateOrder(connection, userId);

            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                BigDecimal price = calculateTotalPrice(bottomId, toppingId, quantity, connectionPool);
                ps.setInt(1, orderId);
                ps.setInt(2, bottomId);
                ps.setInt(3, toppingId);
                ps.setInt(4, quantity);
                ps.setBigDecimal(5, price);

                ps.executeUpdate();

            } catch (SQLException e)
            {
                throw new DatabaseException("Der skete en fejl ved læsning af databasen", e.getMessage());
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Der skete en fejl med orderlinjen", e.getMessage());
        }
    }

    private static int getOrCreateOrder(Connection connection, int userId) throws DatabaseException, SQLException
    {
        int existingOrderId = findActiveOrderId(connection, userId);

        if (existingOrderId != -1)
        {
            return existingOrderId;
        }
        return createNewOrder(connection, userId);
    }

    private static int findActiveOrderId(Connection connection, int userId) throws DatabaseException
    {
        String sql = "SELECT * FROM orders WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt("order_id");
                }
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Der kunne ikke oprettes forbindelse til din ordre.", e.getMessage());
        }
        return -1;
    }

    private static int createNewOrder(Connection connection, int userId) throws SQLException
    {
        String sql = "INSERT INTO Orders (user_id, order_date, total_price) VALUES (?, NOW(), 0.00) RETURNING order_id;";

        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setBigDecimal(2, BigDecimal.ZERO);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt("order_id");
                }
            }
        }
        throw new SQLException("Der skete en fejl ved oprettelse af ordren, prøv igen senere");
    }


    private static BigDecimal calculateTotalPrice(int bottomId, int toppingId, int quantity, ConnectionPool connectionPool) throws DatabaseException
    {
        Bottoms selectedBottom = getBottomById(bottomId, connectionPool);
        Toppings selectedTopping = getToppingById(toppingId, connectionPool);
        return (selectedBottom.getPrice().add(selectedTopping.getPrice())).multiply(BigDecimal.valueOf(quantity));
    }

    public static Bottoms getBottomById(int bottomId, ConnectionPool connectionPool) throws DatabaseException
    {
        Bottoms bottom = null;
        String sql = "SELECT * FROM bottom WHERE bottom_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, bottomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                int id = rs.getInt("bottom_id");
                String name = rs.getString("bottom_name");
                BigDecimal price = rs.getBigDecimal("price");
                bottom = new Bottoms(id, name, price);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return bottom;
    }

    private static Toppings getToppingById(int toppingId, ConnectionPool connectionPool) throws DatabaseException
    {
        Toppings topping = null;
        String sql = "SELECT * FROM topping WHERE topping_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, toppingId);
            ResultSet rs = ps.executeQuery();
            {
                if (rs.next())
                {
                    int id = rs.getInt("topping_id");
                    String name = rs.getString("topping_name");
                    BigDecimal price = rs.getBigDecimal("price");
                    topping = new Toppings(id, name, price);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return topping;
    }
}
