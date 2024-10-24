package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.math.BigDecimal;

public class UserController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.get("createuser", ctx -> ctx.render("createuser.html"));
        app.post("createuser", ctx -> createUser(ctx, connectionPool));
    }

    private static void createUser(Context ctx, ConnectionPool connectionPool)
    {
        String username = ctx.formParam("username");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        String firstName = ctx.formParam("firstname");
        String lastName = ctx.formParam("lastname");
        String email = ctx.formParam("email");
        String postcodeStr = ctx.formParam("postcode");

        int postcode;
        try
        {
            postcode = Integer.parseInt(postcodeStr);
        } catch (NumberFormatException e)
        {
            ctx.attribute("message", "Postnummer skal være et gyldigt tal. Prøv igen");
            ctx.render("createuser.html");
            return;
        }

        if (password1.equals(password2))
        {
            try
            {
                BigDecimal defaultBalance = new BigDecimal(500.00);

                UserMapper.createuser(username, password1, firstName, lastName, email, defaultBalance, postcode, connectionPool);

                ctx.attribute("message", "Du er hermed oprettet med brugernavn: " + username + ". Nu skal du logge på.");
                ctx.render("index.html");
            } catch (DatabaseException e)
            {
                ctx.attribute("message", "Dit brugernavn findes allerede. Prøv igen, eller log ind");
                ctx.render("createuser.html");
            }
        } else
        {
            ctx.attribute("message", "Dine to passwords matcher ikke! Prøv igen");
            ctx.render("createuser.html");
        }
    }

    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void login(Context ctx, ConnectionPool connectionPool)
    {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        try
        {
            User user = UserMapper.login(username, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.attribute("message", "Du er nu logget ind");
            ctx.render("index.html");
        } catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }

    }
}
