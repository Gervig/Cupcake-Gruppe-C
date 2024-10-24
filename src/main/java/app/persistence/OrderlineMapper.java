package app.persistence;

import app.entities.Orderline;
import app.entities.Orders;

import java.util.ArrayList;
import java.util.List;

public class OrderlineMapper
{
    public static List<Orderline> getAllOrderlinePerUser(int userId, ConnectionPool connectionPoo)
    {
        List<Orderline> orderlineList = new ArrayList<>();

        //TODO get a userId from order

        String sql = "";

        return orderlineList;
    }
}
