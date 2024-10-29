package app.controllers;

import app.entities.Bottoms;
import app.entities.Toppings;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ItemMapper;
import io.javalin.Javalin;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.context.WebContext;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class ItemController
{

    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.get("/orderCupcakes", ctx -> {
            List<Bottoms> bottomsList = ItemMapper.getBottoms(connectionPool);
            List<Toppings> toppingsList = ItemMapper.getToppings(connectionPool);

            ctx.attribute("bottomsList", bottomsList);
            ctx.attribute("toppingsList", toppingsList);
            ctx.render("orderCupcakes.html");
        });
        app.get("listOfCupcakes", ctx -> {
            getBottomNames(ctx, connectionPool);
            getToppingsNames(ctx, connectionPool);
            ctx.render("listOfCupcakes.html");
        });
        app.post("/createOrderLine", ctx -> {
            User user = ctx.sessionAttribute("currentUser");
            if (user == null) {
                ctx.attribute("message", "You need to log in to add items to your cart.");
                ctx.render("login.html"); // Redirect to login if user is not logged in
                return; // Exit the handler
            }

            try {
                int userId = user.getUserId();
                int bottomId = Integer.parseInt(ctx.formParam("bottom")); // Match the HTML input names
                int toppingId = Integer.parseInt(ctx.formParam("topping"));
                int quantity = Integer.parseInt(ctx.formParam("quantity"));

                // Call method to create the Orderline
                ItemMapper.createOrderLine(bottomId, toppingId, quantity, userId, connectionPool);

                ctx.attribute("message", "Order line added successfully!");
                ctx.redirect("/orderCupcakes"); // Redirect back to the order page to see the updated cart
            } catch (NumberFormatException e) {
                ctx.attribute("message", "Invalid input. Please ensure all fields are filled correctly.");
                ctx.render("orderCupcakes.html"); // Re-render the page with the message
            } catch (DatabaseException e) {
                ctx.attribute("message", "Failed to add order line: " + e.getMessage());
                ctx.render("orderCupcakes.html"); // Re-render with error message
            }
        });

    }

    public static void createOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {

    }

    public static void getBottomNames(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try
        {
            List<Bottoms> bottomsList = ItemMapper.getBottoms(connectionPool);
            ctx.attribute("bottomsList", bottomsList);
            ctx.render("orderCupcakes.html");
        }
        catch (DatabaseException e)
        {
            ctx.attribute("message","Kunne ikke finde listen af bunde i databasen");
        }
    }

    public static void getToppingsNames(Context ctx, ConnectionPool connectionPool) throws DatabaseException
    {
        try
        {
            List<Toppings> toppingsList = ItemMapper.getToppings(connectionPool);
            ctx.attribute("toppingsList", toppingsList);
            ctx.render("orderCupcakes.html");
        } catch (DatabaseException e)
        {
            ctx.attribute("message", "Kunne ikke finde listen af toppe i databasen");
        }
    }
}
