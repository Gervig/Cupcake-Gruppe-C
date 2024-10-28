package app.controllers;

import app.entities.Order;
import app.entities.Orderline;
import app.entities.Orders;
import app.entities.User;
import app.persistence.OrderlineMapper;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrdersMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/listOfOrderlines", ctx -> getAllOrderlines(ctx, connectionPool));
    }

    public static void getAllOrderlines(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Orderline> orderlines = OrderlineMapper.getAllOrderlines(connectionPool);
            ctx.attribute("orderlines", orderlines);
            ctx.render("listOfOrders.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Kunne ikke hente ordrelinjer: " + e.getMessage());
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

    public static void checkOutCart(Context ctx, ConnectionPool connectionPool)
    {
        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.attribute("message", "Du skal logge ind for at se din indkøbskurv."); //Hvis der nu er en fejl med login
            ctx.redirect("/login"); // Du bliver sendt tilbage til login hvis du ikke er logget ind
            return;
        }

        List<Orderline> orders = new ArrayList<>();

        try {
            orders = OrderlineMapper.getAllOrderlinePerUser(user.getUserId(), connectionPool);
            ctx.attribute("orders", orders);
            ctx.attribute("shoppingBasketMessage", "Her er din indkøbskurv");
            ctx.render("shoppingBasket.html");
        } catch (DatabaseException e) {
            ctx.attribute("shoppingBasketMessage", "Kunne ikke finde dine ordrer!");
            ctx.render("orderCupcakes.html");
        }
    }


    private static void createNewOrder(Context ctx, ConnectionPool connectionPool) {
    }
}
