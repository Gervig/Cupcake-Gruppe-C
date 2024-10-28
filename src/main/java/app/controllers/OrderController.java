package app.controllers;

import app.entities.Order;
import app.entities.OrderLine;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("listOfOrders", ctx -> getAllOrders(ctx, connectionPool));
    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Order> orders = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("orders", orders);
            ctx.render("listOfOrders.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Kunne ikke hente ordre: " + e.getMessage());
            ctx.status(500).render("/templates/error.html");
        }
    }


    private static void getOrderById(Context ctx, ConnectionPool connectionPool) {
        try {
            int orderId = Integer.parseInt(ctx.pathParam("id"));
            List<Order> order = OrderMapper.getOrdersByUserId(orderId, connectionPool);

            if (order != null) {
                ctx.attribute("order", order);
                ctx.render("/templates/order_details.html");
            } else {
                ctx.attribute("message", "Kunne ikkke henter ordre.");
                ctx.status(404).render("/templates/error.html");
            }
        } catch (NumberFormatException e) {
            ctx.attribute("message", "Forkert formatering af order ID.");
            ctx.status(400).render("/templates/error.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Kunne ikkke henter ordre " + e.getMessage());
            ctx.status(500).render("/templates/error.html");
        }
    }


    private static void createNewOrder(Context ctx, ConnectionPool connectionPool) {
    }
}
