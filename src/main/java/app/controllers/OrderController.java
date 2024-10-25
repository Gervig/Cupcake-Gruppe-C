package app.controllers;

import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderlineMapper;
import app.persistence.OrdersMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class OrderController
{
    public static void checkOutCart(Context ctx, ConnectionPool connectionPool)
    {
        List<Orderline> allOrdersPerUser = new ArrayList<>();
        // Hent form parametre
        String email = ctx.formParam("email");
//        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        //TODO kunne logge ind med email også
        // Check om bruger findes i DB med de angivne username + password
        try
        {
            // maybe don't login again, but we need a current user?
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            allOrdersPerUser = OrderlineMapper.getAllOrderlinePerUser(user.getUserId(), connectionPool);
            ctx.sessionAttribute("orders", allOrdersPerUser);
            // Hvis ja, send videre til forsiden med login besked
            ctx.attribute("shoppingBasketMessage", "Her er din indkøbskurv");
            ctx.render("shoppingBasket.html");
        } catch (DatabaseException e)
        {
            // Hvis nej, send tilbage til login side med fejl besked
            ctx.attribute("shoppingBasketMessage","Kunne ikke finde dine ordrer!");
            ctx.render("orderCupcakes.html");
        }
    }
}
