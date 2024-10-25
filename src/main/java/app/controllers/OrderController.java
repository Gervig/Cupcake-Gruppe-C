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

    //Din gamle kode som jeg ændrede på står her så du kan se hvad der er ændret og tilføjer i forhold til den nye
    /*
{
    List<Orderline> orders = new ArrayList<>();                 Rykkede arraylisten længere ned

    String email = ctx.formParam("email");                      Ændrede så vi kigger efter currentUser i stedet (kunne se at du havde en længere nede)
    String password = ctx.formParam("password");                Tilføjede en metode til at tjekke hvem currentUser er

    try
    {

        User user = UserMapper.login(email, password, connectionPool);      Fjernede denne linje
        ctx.sessionAttribute("currentUser", user);                          Denne ryger også med ud

        orders = OrderlineMapper.getAllOrderlinePerUser(user.getUserId(), connectionPool);      { Det forbliver det samme
        ctx.attribute("orders", orders);
        ctx.attribute("shoppingBasketMessage", "Her er din indkøbskurv");
        ctx.render("shoppingBasket.html");
    } catch (DatabaseException e)
    {
        // Hvis nej, send tilbage til login side med fejl besked
        ctx.attribute("shoppingBasketMessage","Kunne ikke finde dine ordrer!");
        ctx.render("orderCupcakes.html");
    }
}                                                                                               }
*/
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

}
