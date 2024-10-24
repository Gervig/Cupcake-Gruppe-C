package app.persistence;

import app.entities.Orderline;
import app.entities.Orders;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;


public class OrderlineMapper
{
    public static List<Orderline> getAllOrderlinePerUser(int userId, ConnectionPool connectionPool) throws DatabaseException
    {
        List<Orderline> orderlineList = new ArrayList<>();

        //TODO join!

        String sql = "";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
                )
        {
            ps.setInt(1, );
        }

        return orderlineList;
    }
}
