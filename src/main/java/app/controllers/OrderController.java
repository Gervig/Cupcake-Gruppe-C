package app.controller;

import io.javalin.http.Context;
import app.persistence.OrderMapper;
import app.entities.Order;
import app.persistence.ConnectionPool; // Assuming you have a connection pool class
import java.util.List;

public class OrderController {

    private static ConnectionPool connectionPool;

    public OrderController(ConnectionPool pool) {
        connectionPool = pool;
    }

    public static void getAllOrders(Context ctx) {
        try {
            List<Order> orders = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("orders", orders);
            ctx.render("/templates/admin_orders.html");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Kunne ikke hente ordre");
        }
    }
}