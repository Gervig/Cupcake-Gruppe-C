package app.persistence;

import app.entities.Bottoms;
import app.entities.Orders;
import app.entities.Toppings;
import app.exceptions.DatabaseException;


import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CupcakeService
{
    public List<Bottoms> getBottoms(ConnectionPool connectionPool) throws DatabaseException
    {

        List<Bottoms> bottomsList = new ArrayList<>();
        String sql = "SELECT * FROM bottom";

        try (Connection connection = connectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();
                while(rs.next())
                {
                    bottomsList.add(new Bottoms(rs.getInt("bottom_id"), rs.getString("bottom_name"), rs.getFloat("price")));
                }
            } catch (SQLException e)
                {
                    throw new DatabaseException("Der skete en fejl");
                }
        return bottomsList;
    }

    public List<Toppings> getToppings(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Toppings> toppingsList = new ArrayList<>();
        String sql = "SELECT * FROM topping";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                toppingsList.add(new Toppings(rs.getInt("topping_id"), rs.getString("topping_name"), rs.getFloat("price")));
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Der skete en fejl");
        }
        return toppingsList;
    }

    public List<Orders> getOrders(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                ordersList.add(new Orders(rs.getInt("order_id"), rs.getInt("user_id"), rs.getFloat("order_date"), rs.getFloat("total_price")));
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Der skete en fejl");
        }
        return ordersList;
    }

    public void createOrder(int bottomId, int toppingId, ConnectionPool connectionPool) throws DatabaseException
    {
        String insertOrderSQL = "INSERT INTO orders (total_price) VALUES (?)";
        String insertOrderItemSQL = "INSERT INTO orderline (orderline_id, order_id, bottom_id, topping_id, quantity) VALUES (?, ?, ?)";
        double totalPrice = calculateTotalPrice(bottomId, toppingId, connectionPool);

        try (Connection connection = connectionPool.getConnection()) {
            // Start transaction
            connection.setAutoCommit(false);

            // Insert order
            try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderSQL, new String[]{"id"}))
            {
                orderStatement.setDouble(1, totalPrice);
                orderStatement.executeUpdate();

                // Get the generated order ID
                try (var rs = orderStatement.getGeneratedKeys())
                {
                    if (rs.next()) {
                        int orderId = rs.getInt(1);

                        // Insert order items
                        try (PreparedStatement orderItemStatement = connection.prepareStatement(insertOrderItemSQL)) {
                            orderItemStatement.setInt(1, orderId);
                            orderItemStatement.setInt(2, bottomId);
                            orderItemStatement.setInt(3, toppingId);
                            orderItemStatement.executeUpdate();
                        }
                    }
                }
            }

            // Commit transaction
            connection.commit();
        } catch (SQLException e)
        {
            throw new DatabaseException("Der skete en fejl");
        }
    }

    private double calculateTotalPrice(int bottomId, int toppingId, ConnectionPool connectionPool) throws DatabaseException
    {
        Bottoms selectedBottom = getBottomById(bottomId, connectionPool);
        Toppings selectedTopping = getToppingById(toppingId, connectionPool);
        return selectedBottom.getPrice() + selectedTopping.getPrice();
    }

    public Bottoms getBottomById(int bottomId, ConnectionPool connectionPool) throws DatabaseException
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
                    bottom = new Bottoms(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"));
                }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return bottom;
    }

    private Toppings getToppingById(int toppingId, ConnectionPool connectionPool) throws DatabaseException
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
                    topping = new Toppings(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"));
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return topping;
    }
}
