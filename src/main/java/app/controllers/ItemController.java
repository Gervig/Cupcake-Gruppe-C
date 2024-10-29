package app.controllers;

import app.entities.Bottoms;
import app.entities.Toppings;
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
        app.get("orderCupcakes", ctx -> {
            getBottomNames(ctx, connectionPool);
            getToppingsNames(ctx, connectionPool);
        });
        app.get("listOfCupcakes", ctx -> {
            getBottomNames(ctx, connectionPool);
            getToppingsNames(ctx, connectionPool);
            ctx.render("listOfCupcakes.html");
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
